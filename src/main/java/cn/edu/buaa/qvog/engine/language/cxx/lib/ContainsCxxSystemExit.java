package cn.edu.buaa.qvog.engine.language.cxx.lib;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxSystemExit;
import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsCxxSystemExit implements IValuePredicate {
    @Override
    public boolean test(Value value) {
//        return new ContainsFunctionCall("exit")
//                .or(e -> e instanceof CxxSystemExit).test(value);

        return value instanceof CxxSystemExit;
    }
}
