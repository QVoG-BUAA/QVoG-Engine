package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArkTS;

import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;

import java.util.List;

public class ArkTSNewExpression extends Expression implements HasOperator{
    // in case of new an array
    private final List<Reference> subs;
    private Boolean isVectored;

    public ArkTSNewExpression(List<Reference> subs, Boolean isVectored) {
        this.subs = subs;
        this.isVectored = isVectored;
    }

    public ArkTSNewExpression(Type type, List<Reference> subs, Boolean isVectored) {
        super(type);
        this.subs = subs;
        this.isVectored = isVectored;
    }

    @Override
    public String getOperator() {
        return "new";
    }
}
