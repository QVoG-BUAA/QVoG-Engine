package cn.edu.engine.qvog.engine.dsl.fluent.clause;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.db.gremlin.IGremlinDb;
import cn.edu.engine.qvog.engine.dsl.data.DataTable;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.data.IndexTypes;
import cn.edu.engine.qvog.engine.exception.TraversalException;
import cn.edu.engine.qvog.engine.ml.ILearningApi;
import cn.edu.engine.qvog.engine.ml.PredictTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LearningFromDescriptor extends BaseFromDescriptor {
    private final String cacheTag;
    private final PredictTypes type;
    private ITable table = null;

    LearningFromDescriptor(String alias, PredictTypes type, String cacheTag) {
        super(alias);
        this.type = type;
        this.cacheTag = cacheTag == null ? "ml" : cacheTag;
    }

    @Override
    public String getCacheTag() {
        return cacheTag;
    }

    @Override
    public ITable fetchData(IDbContext dbContext) {
        if (table != null) {
            return table;
        }

        if (cacheTag != null) {
            var cache = dbContext.getCacheProxy();
            var cached = cache.get(makeCacheTag(cacheTag, type));
            if (cached != null) {
                table = (ITable) cached;
                table.amend(alias);
                return table;
            }
        }

        // fetch via api.
        var api = Environment.getInstance().getLearningApi();
        var map = filter(dbContext.getGremlinDb(), api);
        table = map.get(type);
        table.amend(alias);

        if (cacheTag != null) {
            for (var entry : map.entrySet()) {
                dbContext.getCacheProxy().put(makeCacheTag(cacheTag, entry.getKey()), entry.getValue());
            }
        }

        return table;
    }

    private static String makeCacheTag(String tag, PredictTypes type) {
        return tag + ":" + type.name();
    }

    private Map<PredictTypes, ITable> filter(IGremlinDb db, ILearningApi api) {
        ITable table = DataTable.builder().withName(alias)
                .withColumn(builder -> builder.withName(alias)
                        .withIndex(IndexTypes.ValueIndex)).build();
        Map<PredictTypes, ITable> map = Map.of(
                PredictTypes.Source, table,
                PredictTypes.Sink, table.duplicate());

        var helper = Environment.getInstance().getGraphHelper();

        List<Value> values = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        try (var traversal = db.g().V()) {
            traversal.asAdmin().clone().forEachRemaining(vertex -> {
                var value = helper.toValue(vertex);
                if (!value.isUnknown()) {
                    values.add(value);
                    codes.add(value.getNode().code());
                }
            });
        } catch (Exception e) {
            throw new TraversalException("Failed to fetch table", e);
        }

        List<String> bulk = new ArrayList<>();
        int bulkSize = 100;
        int offset = 0;
        for (var code : codes) {
            bulk.add(code);
            if (bulk.size() == bulkSize) {
                filterBulk(api, bulk, values, map, offset);
                bulk.clear();
                offset += bulkSize;
            }
        }
        if (!bulk.isEmpty()) {
            filterBulk(api, bulk, values, map, offset);
        }

        return map;
    }

    private void filterBulk(ILearningApi api, List<String> bulk, List<Value> values, Map<PredictTypes, ITable> map, int offset) {
        var results = api.predict(bulk);
        for (int i = 0; i < results.size(); i++) {
            var result = results.get(i);
            if (result.getSecond() != PredictTypes.None) {
                map.get(result.getSecond()).addRow(values.get(offset + i));
            }
        }
    }
}
