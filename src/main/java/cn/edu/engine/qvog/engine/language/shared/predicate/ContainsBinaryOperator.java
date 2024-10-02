package cn.edu.engine.qvog.engine.language.shared.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.BinaryOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

import java.util.function.Predicate;

public class ContainsBinaryOperator implements IValuePredicate {
    private final String operator;
    private final Predicate<Expression> p1;
    private final Predicate<Expression> p2;

    public ContainsBinaryOperator(Predicate<Expression> p1, Predicate<Expression> p2, String operator) {
        this.p1 = p1;
        this.p2 = p2;
        this.operator = operator;
    }

    public ContainsBinaryOperator(Predicate<Expression> p1, String operator) {
        this.p1 = p1;
        this.p2 = null;
        this.operator = operator;
    }

    public ContainsBinaryOperator(String operator) {
        this.p1 = null;
        this.p2 = null;
        this.operator = operator;
    }

    @Override
    public boolean test(Value value) {
        if (this.p1 != null && this.p2 != null) {
            if (this.operator != null) {
                return value.toStream().anyMatch(v -> v instanceof BinaryOperator binaryOperator &&
                        binaryOperator.getOperator().equals(operator) &&
                        (p1.test(binaryOperator.getLeft()) && p2.test(binaryOperator.getRight()) ||
                                p2.test(binaryOperator.getLeft()) && p1.test(binaryOperator.getRight())));
            }
            return value.toStream().anyMatch(v -> v instanceof BinaryOperator binaryOperator &&
                    (p1.test(binaryOperator.getLeft()) && p2.test(binaryOperator.getRight()) ||
                            p2.test(binaryOperator.getLeft()) && p1.test(binaryOperator.getRight())));
        }
        if (this.p1 != null) {
            if (this.operator != null) {
                return value.toStream().anyMatch(v -> v instanceof BinaryOperator binaryOperator &&
                        binaryOperator.getOperator().equals(operator) &&
                        (p1.test(binaryOperator.getLeft()) || p1.test(binaryOperator.getRight())));
            }
            return value.toStream().anyMatch(v -> v instanceof BinaryOperator binaryOperator &&
                    (p1.test(binaryOperator.getLeft()) || p1.test(binaryOperator.getRight())));
        }
        return value.toStream().anyMatch(v -> v instanceof BinaryOperator binaryOperator
                && binaryOperator.getOperator().equals(this.operator));
    }
}
