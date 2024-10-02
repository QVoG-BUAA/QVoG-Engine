package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.MemberExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

/**
 * a.b.c like this
 */
public class FieldReferenceExpressionHandler extends BaseHandler implements IValueHandler<MemberExpression> {
    @Override
    public MemberExpression build(JSONObject json, IValueFactory factory) {
        String name = JsonHelper.getValue(json, "field");
        Expression base = factory.build(
                JsonHelper.getObject(json, "parent"),
                UnknownExpression.DEFAULT);

        return new MemberExpression(name, base);
    }
}
