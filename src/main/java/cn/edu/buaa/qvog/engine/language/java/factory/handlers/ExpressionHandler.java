package cn.edu.buaa.qvog.engine.language.java.factory.handlers;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class ExpressionHandler extends BaseHandler implements IValueHandler<Expression> {
    @Override
    public Expression build(JSONObject json, IValueFactory factory) {
        return factory.build(JsonHelper.getObject(json, "value"), UnknownExpression.DEFAULT);
    }
}
