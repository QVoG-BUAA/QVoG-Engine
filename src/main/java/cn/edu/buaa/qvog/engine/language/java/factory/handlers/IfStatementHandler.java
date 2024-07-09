package cn.edu.buaa.qvog.engine.language.java.factory.handlers;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.IfStatement;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class IfStatementHandler extends BaseHandler implements IValueHandler<IfStatement> {
    @Override
    public IfStatement build(JSONObject json, IValueFactory factory) {
        Expression condition = factory.build(
                JsonHelper.getObject(json, "test"),
                UnknownExpression.DEFAULT);

        return new IfStatement(condition);
    }
}
