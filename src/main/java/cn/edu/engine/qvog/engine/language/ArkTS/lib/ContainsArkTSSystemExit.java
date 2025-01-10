package cn.edu.engine.qvog.engine.language.ArkTS.lib;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArkTS.virtuals.ArkTSSystemExit;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsArkTSSystemExit implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return value instanceof ArkTSSystemExit;
    }
}
