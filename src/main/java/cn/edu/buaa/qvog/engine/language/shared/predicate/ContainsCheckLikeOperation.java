package cn.edu.buaa.qvog.engine.language.shared.predicate;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.IfStatement;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsCheckLikeOperation implements IValuePredicate {
    @Override
    public boolean test(Value v) {
        return v.toStream().anyMatch(
                v1 -> (v1 instanceof IfStatement
                        || v1 instanceof CallExpression
                ));
    }
}
