package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.AssignExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Handler for {@link AssignExpression}
 */
public class AssignExpressionHandler extends BaseHandler implements IValueHandler<AssignExpression> {
    @Override
    public AssignExpression build(JSONObject json, IValueFactory factory) {
        JSONObject targetObject = JsonHelper.getObject(json, "target");
        JSONObject valueObject = JsonHelper.getObject(json, "value");

        ArrayList<JSONObject> targets = new ArrayList<>();
        targets.add(targetObject);

        ArrayList<JSONObject> values = new ArrayList<>();
        values.add(valueObject);
        return new AssignExpression(
                targets.stream().map(x -> factory.<Expression, UnknownExpression>build(x, UnknownExpression.DEFAULT)).toList(),
                values.stream().map(x -> factory.<Expression, UnknownExpression>build(x, UnknownExpression.DEFAULT)).toList()
        );
    }
}
