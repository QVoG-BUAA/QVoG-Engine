package cn.edu.engine.qvog.engine.helper.graph;

import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.gremlin.IGremlinDb;
import cn.edu.engine.qvog.engine.dsl.data.DataTable;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.data.IndexTypes;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.exception.TraversalException;

/**
 * I'm not sure if it has a proper name, but it's a filter for the graph.
 */
public class GraphFilter {
    private final IGraphHelper helper;
    private IGremlinDb db;
    private IValuePredicate predicate;

    private GraphFilter() {
        helper = Environment.getInstance().getGraphHelper();
    }

    public static GraphFilter open() {
        return new GraphFilter();
    }

    public GraphFilter withDb(IGremlinDb db) {
        this.db = db;
        return this;
    }

    public GraphFilter withPredicate(IValuePredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public ITable filter(String name) {
        if (db == null || predicate == null) {
            throw new IllegalStateException("Filter must be associated to database and predicate.");
        }

        ITable table = DataTable.builder().withName(name)
                .withColumn(builder -> builder.withName(name)
                        .withIndex(IndexTypes.ValueIndex)).build();

        try (var traversal = db.V()) {
            traversal.asAdmin().clone().forEachRemaining(vertex -> {
                var value = helper.toValue(vertex);
                if (value.toStream().anyMatch(predicate::test)) {
                    table.addRow(value);
                }
            });
        } catch (Exception e) {
            throw new TraversalException("Failed to fetch table", e);
        }

        return table;
    }
}
