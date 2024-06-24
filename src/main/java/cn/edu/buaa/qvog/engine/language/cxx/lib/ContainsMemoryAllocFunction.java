package cn.edu.buaa.qvog.engine.language.cxx.lib;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsMemoryAllocFunction implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return value.toStream().anyMatch(v -> v instanceof CallExpression callExpression &&
                ("malloc".equals(callExpression.getFunction().getName())
                        || "calloc".equals(callExpression.getFunction().getName())
                        || "realloc".equals(callExpression.getFunction().getName())
                        || "wcsdup".equals(callExpression.getFunction().getName()))
        );
    }
}
