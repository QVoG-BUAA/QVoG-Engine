package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;

import java.util.List;

/**
 * type -> the type of return value
 */
public class CxxNewExpression extends Expression implements HasOperator {
    // in case of new an array
    private final List<Reference> subs;
    private Boolean isVectored;

    public CxxNewExpression(List<Reference> subs, Boolean isVectored) {
        this.subs = subs;
        this.isVectored = isVectored;
    }

    public CxxNewExpression(Type type, List<Reference> subs, Boolean isVectored) {
        super(type);
        this.subs = subs;
        this.isVectored = isVectored;
    }

    public Boolean getVectored() {
        return isVectored;
    }

    public void setVectored(Boolean vectored) {
        isVectored = vectored;
    }

    public List<Reference> getSubs() {
        return subs;
    }

    @Override
    public String getOperator() {
        return "new";
    }
}
