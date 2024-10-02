package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.values.constrains.IsUnknown;

/**
 * This class is used to represent an unknown expression.
 */
public final class UnknownExpression extends Expression implements IsUnknown {
    public static final UnknownExpression DEFAULT = new UnknownExpression();
}
