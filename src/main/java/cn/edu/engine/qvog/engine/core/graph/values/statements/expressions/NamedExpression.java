package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasName;

/**
 * Expression that has a name. For example, in Python, we can have
 * {@code foo(param=1)} where {@code param=1} is a named expression.
 */
public class NamedExpression extends Expression implements HasName {
    private final String name;
    private final Expression expression;

    public NamedExpression(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public NamedExpression(Type type, String name, Expression expression) {
        super(type);
        this.name = name;
        this.expression = expression;
    }

    @Override
    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }
}
