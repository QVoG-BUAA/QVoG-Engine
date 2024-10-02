package cn.edu.engine.qvog.engine.dsl.lib.engine;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.ValueFactory;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.DbContext;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.db.cache.ICacheProxy;
import cn.edu.engine.qvog.engine.db.cache.NoCacheProxy;
import cn.edu.engine.qvog.engine.db.cache.RedisCacheProxy;
import cn.edu.engine.qvog.engine.db.gremlin.GremlinDb;
import cn.edu.engine.qvog.engine.db.gremlin.IGremlinDb;
import cn.edu.engine.qvog.engine.dsl.IQueryable;
import cn.edu.engine.qvog.engine.dsl.fluent.query.CompleteQuery;
import cn.edu.engine.qvog.engine.dsl.fluent.query.IQueryDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.query.QueryDescriptor;
import cn.edu.engine.qvog.engine.dsl.lib.format.DefaultResultFormatter;
import cn.edu.engine.qvog.engine.dsl.lib.format.IResultFormatter;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.helper.graph.CachedGraphHelper;
import cn.edu.engine.qvog.engine.helper.graph.IGraphHelper;
import cn.edu.engine.qvog.engine.ml.ILearningApi;
import cn.edu.engine.qvog.engine.ml.LearningApiBuilder;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Function;

public class QueryEngine {
    private static QueryEngine instance;
    private IDbContext dbContext;
    private long totalExecutionTime = 0;

    private IResultFormatter formatter = new DefaultResultFormatter();
    private PrintStream output = System.out;
    private String style = "console";

    private QueryEngine(String filename) {
        JSONObject config = loadConfiguration(filename == null ? "config.json" : filename);

        // Initialize database context.
        initializeGremlinDb(JsonHelper.getObject(config, "gremlin"));
        initializeCache(JsonHelper.getObject(config, "cache"));
        initializeDbContext();

        // Initialize Machine Learning model.
        initializeLearning(JsonHelper.tryGetObject(config, "learning"));

        // Initialize core dependencies.
        initializeDependencyInjections();
    }

    private JSONObject loadConfiguration(String filename) {
        return JsonHelper.load(filename);
    }

    private void initializeGremlinDb(JSONObject config) {
        GremlinDb gremlinDb = GremlinDb.connect(config);
        Environment.getInstance().getServiceProvider().bind(IGremlinDb.class, gremlinDb);
    }

    private void initializeCache(JSONObject config) {
        String type = JsonHelper.getValue(config, "type").toLowerCase();
        if (type.equals("none")) {
            Environment.getInstance().getServiceProvider().bind(ICacheProxy.class, new NoCacheProxy());
        } else if (type.equals("redis")) {
            var redis = RedisCacheProxy.connect(JsonHelper.getObject(config, "redis"));
            Environment.getInstance().getServiceProvider().bind(ICacheProxy.class, redis);
        } else {
            throw new IllegalArgumentException("Unknown cache type: " + type);
        }
    }

    private void initializeDbContext() {
        var env = Environment.getInstance();

        // Pre-build the service provider to make sure the fundamental dependencies are ready.
        env.getServiceProvider().build();

        var gremlinDb = env.get(IGremlinDb.class);
        var cache = env.get(ICacheProxy.class);
        dbContext = new DbContext(gremlinDb, cache);
        env.getServiceProvider().bind(IDbContext.class, dbContext);
    }

    private void initializeLearning(JSONObject config) {
        ILearningApi learningApi = LearningApiBuilder.connect(config);
        Environment.getInstance().getServiceProvider().bind(ILearningApi.class, learningApi);
    }

    private void initializeDependencyInjections() {
        Environment.getInstance().getServiceProvider()
                .bind(IGraphHelper.class, CachedGraphHelper.class)
                .bind(IValueFactory.class, ValueFactory.class);
    }

    public static QueryEngine getInstance() {
        return getInstance(null);
    }

    public static QueryEngine getInstance(String filename) {
        if (instance == null) {
            instance = new QueryEngine(filename);
        }
        return instance;
    }

    public QueryEngine withFormatter(IResultFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public QueryEngine withOutput(PrintStream output) {
        this.output = output;
        return this;
    }

    public QueryEngine withStyle(String style) {
        this.style = style;
        return this;
    }

    public QueryEngine execute(IQueryable query) {
        return execute(query.getQueryName(), query);
    }

    public QueryEngine execute(String name, IQueryable query) {
        Environment.getInstance().getServiceProvider().build();

        try (StringOutputStream out = new StringOutputStream()) {
            long start = System.currentTimeMillis();
            query.run().display(style, new PrintStream(out));
            long end = System.currentTimeMillis();
            totalExecutionTime += end - start;

            QueryResult result = new QueryResult(name, out.toString(), end - start);
            output.println(formatter.format(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public QueryEngine execute(String name, Function<IQueryDescriptor, CompleteQuery> query) {
        return execute(name, new IQueryable() {

            @Override
            public IQueryable withDatabase(IDbContext dbContext) {
                return this;
            }

            @Override
            public CompleteQuery run() {
                return query.apply(QueryDescriptor.open());
            }
        });
    }

    public void close() {
        output.println(formatter.formatTotalTime(totalExecutionTime));
        dbContext.close();
        System.exit(0);
    }

    private static class StringOutputStream extends OutputStream {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public void write(int b) throws IOException {
            this.sb.append((char) b);
        }

        @Override
        public String toString() {
            return this.sb.toString();
        }
    }
}
