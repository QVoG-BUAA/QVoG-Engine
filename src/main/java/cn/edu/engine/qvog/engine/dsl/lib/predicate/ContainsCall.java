package cn.edu.engine.qvog.engine.dsl.lib.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.CallExpression;

import java.util.function.Predicate;

/**
 * Predicate to check if a node contains a function call.
 * It uses an optional call back to perform additional checks.
 */
public class ContainsCall implements IValuePredicate {
    private final Predicate<CallExpression> predicate;

    public ContainsCall() {
        this(null);
    }

    public ContainsCall(Predicate<CallExpression> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(Value value) {
        if (predicate == null) {
            return value.toStream().anyMatch(v -> v instanceof CallExpression);
        } else {
            return value.toStream().anyMatch(v -> {
                if (v instanceof CallExpression call) {
                    return predicate.test(call);
                }
                return false;
            });
        }
    }
}
