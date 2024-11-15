package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArkTS;

import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnaryOperator;

public class ArkTSDeleteExpression extends UnaryOperator {
    private Boolean isVectored;
    //不清楚这个字段有什么用

    public ArkTSDeleteExpression(Expression operand, Boolean isVectored) {
        super(operand, "delete");
        this.isVectored = isVectored;
    }
}
