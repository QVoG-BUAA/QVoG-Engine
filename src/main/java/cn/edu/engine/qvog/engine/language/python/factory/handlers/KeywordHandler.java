package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.NamedExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class KeywordHandler extends BaseHandler implements IValueHandler<NamedExpression> {
    @Override
    public NamedExpression build(JSONObject json, IValueFactory factory) {
        String name = JsonHelper.getValue(json, "arg");
        Expression expression = factory.build(
                JsonHelper.getObject(json, "value"),
                UnknownExpression.DEFAULT);

        return new NamedExpression(name, expression);
    }
}
