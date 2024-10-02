package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx;

import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnaryOperator;

public class CxxDeleteExpression extends UnaryOperator {
    private Boolean isVectored;

    public CxxDeleteExpression(Expression operand, Boolean isVectored) {
        super(operand, "delete");
        this.isVectored = isVectored;
    }

    public Boolean getVectored() {
        return isVectored;
    }

    public void setVectored(Boolean vectored) {
        isVectored = vectored;
    }
}
