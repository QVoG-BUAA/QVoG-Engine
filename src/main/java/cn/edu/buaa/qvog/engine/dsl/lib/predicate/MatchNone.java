package cn.edu.buaa.qvog.engine.dsl.lib.predicate;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;

public class MatchNone implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return false;
    }
}
