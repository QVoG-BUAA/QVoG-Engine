package cn.edu.engine.qvog.engine.core.graph.values.constrains;

/**
 * Interface for all AST nodes that have an operator, e.g., +, -, *, /, etc.
 * <p>
 * WARNING: It can also be described in text, e.g., "Add", "Gt".
 */
public interface HasOperator {
    String getOperator();
}
