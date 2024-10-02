package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.BinaryOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * Take care of the compare node, which has the type of "Compare". It is
 * actually a binary operator, but in a different format.
 */
public class CompareHandler extends BaseHandler implements IValueHandler<BinaryOperator> {
    @Override
    public BinaryOperator build(JSONObject json, IValueFactory factory) {
        var leftObject = JsonHelper.getObject(json, "left");
        var rightArray = JsonHelper.getObjectElements(JsonHelper.getArray(json, "comparators"));
        if (rightArray.size() != 1) {
            throw new IllegalStateException("Compare should have 1 comparator: " + json);
        }
        var rightObject = rightArray.get(0);
        var opArray = JsonHelper.getObjectElements(JsonHelper.getArray(json, "ops"));
        if (opArray.size() != 1) {
            throw new IllegalStateException("Compare should have 1 op: " + json);
        }
        var opObject = opArray.get(0);

        Expression left = factory.build(leftObject, UnknownExpression.DEFAULT);
        Expression right = factory.build(rightObject, UnknownExpression.DEFAULT);
        var operator = getType(opObject);

        return new BinaryOperator(left, right, operator);
    }
}
