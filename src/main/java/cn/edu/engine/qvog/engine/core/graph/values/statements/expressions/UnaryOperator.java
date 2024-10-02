package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasOperator;

import java.util.Collection;

/**
 * Represent unary operator.
 */
public class UnaryOperator extends Expression implements HasOperator {
    private final Expression operand;
    private final String operator;

    public UnaryOperator(Expression operand, String operator) {
        this(operand.getType(), operand, operator);
    }

    public UnaryOperator(Type type, Expression operand, String operator) {
        super(type);
        this.operand = operand;
        this.operand.setParent(this);

        this.operator = operator;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.add(operand);
        operand.addChildren(children);
    }
}
