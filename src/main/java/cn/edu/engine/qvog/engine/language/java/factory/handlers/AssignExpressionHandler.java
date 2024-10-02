package cn.edu.engine.qvog.engine.language.java.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.AssignExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

import java.util.List;

public class AssignExpressionHandler extends BaseHandler implements IValueHandler<AssignExpression> {
    @Override
    public AssignExpression build(JSONObject json, IValueFactory factory) {
        var targets = JsonHelper.getObjectElements(JsonHelper.getArray(json, "targets"));
        var value = JsonHelper.getObject(json, "value");

        return new AssignExpression(
                targets.stream().map(x -> factory.<Expression, UnknownExpression>build(x, UnknownExpression.DEFAULT)).toList(),
                List.of(factory.<Expression, UnknownExpression>build(value, UnknownExpression.DEFAULT))
        );
    }
}
