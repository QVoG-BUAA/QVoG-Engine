package cn.edu.engine.qvog.engine.language.cxx.lib;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxSystemExit;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsCxxSystemExit implements IValuePredicate {
    @Override
    public boolean test(Value value) {
//        return new ContainsFunctionCall("exit")
//                .or(e -> e instanceof CxxSystemExit).test(value);

        return value instanceof CxxSystemExit;
    }
}
