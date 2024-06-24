package cn.edu.buaa.qvog.engine.language.shared.predicate;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnaryOperator;
import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsUnaryOperator implements IValuePredicate {
    private final String pattern;

    public ContainsUnaryOperator(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(Value value) {
        if (pattern == null) {
            return value.toStream().anyMatch(v -> v instanceof UnaryOperator);
        } else {
            return value.toStream().anyMatch(v -> v instanceof UnaryOperator unary &&
                    unary.getOperator().equals(pattern));
        }
    }
}
