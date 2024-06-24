package cn.edu.buaa.qvog.engine.language.shared.predicate;

import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.DeclarationStatement;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.AssignExpression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsDefineLikeOperation implements IValuePredicate {
    @Override
    public boolean test(Value v) {
        return v.toStream().anyMatch(
                v1 -> (v1 instanceof AssignExpression
                        || v1 instanceof CallExpression
                        || v1 instanceof DeclarationStatement declarationStatement
                        && declarationStatement.hasValue()
                ));
    }

    @Override
    public IValuePredicate or(IValuePredicate other) {
        return IValuePredicate.super.or(other);
    }
}
