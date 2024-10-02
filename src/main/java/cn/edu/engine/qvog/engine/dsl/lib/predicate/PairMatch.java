package cn.edu.engine.qvog.engine.dsl.lib.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.IRowPredicate;
import cn.edu.engine.qvog.engine.ml.ILearningApi;

import java.util.Map;

public class PairMatch implements IRowPredicate {
    private final String source;
    private final String sink;
    private final ILearningApi api;

    public PairMatch(String source, String sink) {
        this.source = source;
        this.sink = sink;
        api = Environment.getInstance().getLearningApi();
    }

    @Override
    public boolean test(Map<String, Object> row) {
        if (row.get(source) instanceof Value lhs) {
            if (row.get(sink) instanceof Value rhs) {
                return api.match(lhs.getNode().code(), rhs.getNode().code());
            }
        }
        return false;
    }
}
