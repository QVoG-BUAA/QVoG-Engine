package cn.edu.buaa.qvog.engine.dsl.lib.predicate;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;

public class MatchAll implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return true;
    }
}
