package cn.edu.engine.qvog.engine.core.graph.values.constrains;

import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;

/**
 * Interface for all values that have a base. Most cases are member access.
 */
public interface HasBase {
    Expression getBase();
}
