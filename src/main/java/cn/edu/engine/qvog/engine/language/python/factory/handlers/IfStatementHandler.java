package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.IfStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * Handler for {@link IfStatement}. Only keep the condition.
 */
public class IfStatementHandler extends BaseHandler implements IValueHandler<IfStatement> {
    @Override
    public IfStatement build(JSONObject json, IValueFactory factory) {
        Expression condition = factory.build(
                JsonHelper.getObject(json, "test"),
                UnknownExpression.DEFAULT);

        return new IfStatement(condition);
    }
}
