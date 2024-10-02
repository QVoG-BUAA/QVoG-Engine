package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.AssignExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for {@link AssignExpression}
 */
public class AssignExpressionHandler extends BaseHandler implements IValueHandler<AssignExpression> {
    @Override
    public AssignExpression build(JSONObject json, IValueFactory factory) {
        JSONObject targetObject = JsonHelper.getObject(JsonHelper.getArray(json, "targets"), 0);
        List<JSONObject> targets;
        if (getType(targetObject).equals("Tuple")) {
            targets = JsonHelper.getObjectElements(JsonHelper.getArray(targetObject, "elts"));
        } else {
            targets = JsonHelper.getObjectElements(JsonHelper.getArray(json, "targets"));
        }

        JSONObject valueObject = JsonHelper.getObject(json, "value");
        List<JSONObject> values;
        if (getType(valueObject).equals("Tuple")) {
            values = JsonHelper.getObjectElements(JsonHelper.getArray(valueObject, "elts"));
        } else {
            values = new ArrayList<>(List.of(valueObject));
        }

        return new AssignExpression(
                targets.stream().map(x -> factory.<Expression, UnknownExpression>build(x, UnknownExpression.DEFAULT)).toList(),
                values.stream().map(x -> factory.<Expression, UnknownExpression>build(x, UnknownExpression.DEFAULT)).toList()
        );
    }
}
