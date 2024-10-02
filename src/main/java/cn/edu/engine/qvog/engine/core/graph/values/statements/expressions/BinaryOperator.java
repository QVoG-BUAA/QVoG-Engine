package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasOperator;

import java.util.Collection;

/**
 * Represent binary operator.
 */
public class BinaryOperator extends Expression implements HasOperator {
    private final Expression left;
    private final Expression right;
    private final String operator;

    public BinaryOperator(Expression left, Expression right, String operator) {
        this(left.getType(), left, right, operator);
    }

    /**
     * Here we take the type of the left operator by default.
     *
     * @param type     The type of the binary operator.
     * @param left     Left-hand side operator.
     * @param right    Right-hand side operator.
     * @param operator The operator.
     */
    public BinaryOperator(Type type, Expression left, Expression right, String operator) {
        super(type);
        this.left = left;
        this.left.setParent(this);
        this.right = right;
        this.right.setParent(this);
        this.operator = operator;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.add(left);
        children.add(right);
        left.addChildren(children);
        right.addChildren(children);
    }
}
