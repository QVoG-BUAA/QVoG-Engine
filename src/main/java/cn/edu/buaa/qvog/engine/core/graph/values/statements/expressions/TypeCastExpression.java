package cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions;

public class TypeCastExpression extends UnaryOperator {
    public TypeCastExpression(Expression operand, String operator) {
        super(operand, operator);
    }
}
