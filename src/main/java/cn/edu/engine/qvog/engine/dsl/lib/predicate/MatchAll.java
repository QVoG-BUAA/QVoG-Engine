package cn.edu.engine.qvog.engine.dsl.lib.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;

public class MatchAll implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return true;
    }
}
