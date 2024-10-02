package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * All expressions are wrapped be Expr node, so we need to handle it first.
 * This handler will return the content of Expr node.
 */
public class ExpressionHandler extends BaseHandler implements IValueHandler<Expression> {
    @Override
    public Expression build(JSONObject json, IValueFactory factory) {
        return factory.build(JsonHelper.getObject(json, "value"), UnknownExpression.DEFAULT);
    }
}
