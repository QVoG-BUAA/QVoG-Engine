package cn.edu.engine.qvog.engine.language.shared.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.helper.RegexHelper;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * This class is used to check if a node contains a function call.
 * For attribute order, see {@link ContainsAttribute}.
 * This predicate use callback predicate to perform a further
 * check on the function call.
 */
public class ContainsFunctionCall implements IValuePredicate {
    private final Predicate<CallExpression> predicate;
    private final String wildcard;
    private final Pattern pattern;

    public ContainsFunctionCall(String wildcard) {
        this(null, wildcard);
    }

    public ContainsFunctionCall(Predicate<CallExpression> predicate, String wildcard) {
        this.predicate = predicate;
        this.wildcard = wildcard;
        this.pattern = wildcard == null ? null : RegexHelper.wildcardToRegex(wildcard);
    }

    public ContainsFunctionCall(Predicate<CallExpression> predicate) {
        this(predicate, null);
    }

    public ContainsFunctionCall() {
        this(c -> true, null);
    }

    @Override
    public boolean test(Value value) {
        if (predicate == null) {
            if (pattern == null) {
                return value.toStream().anyMatch(v -> v instanceof CallExpression);
            } else {
                return value.toStream().anyMatch(v -> v instanceof CallExpression call &&
                        wildcard.equals(call.getFunction().getName()));
            }
        } else {
            if (pattern == null) {
                return value.toStream().anyMatch(v -> v instanceof CallExpression call &&
                        predicate.test(call));
            } else {
                return value.toStream().anyMatch(v -> v instanceof CallExpression call &&
                        wildcard.equals(call.getFunction().getName()) &&
                        predicate.test(call));
            }
        }
    }
}
