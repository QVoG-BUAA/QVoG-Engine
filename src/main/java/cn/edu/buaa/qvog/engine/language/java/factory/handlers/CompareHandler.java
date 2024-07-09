package cn.edu.buaa.qvog.engine.language.java.factory.handlers;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.BinaryOperator;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class CompareHandler extends BaseHandler implements IValueHandler<BinaryOperator> {
    @Override
    public BinaryOperator build(JSONObject json, IValueFactory factory) {
        var ops = JsonHelper.getArray(json, "ops");
        if (ops.size() != 1) {
            throw new IllegalArgumentException("Compare should have 1 op: " + json);
        }

        var comparators = JsonHelper.getArray(json, "comparators");
        if (comparators.size() != 1) {
            throw new IllegalArgumentException("Compare should have 1 comparator: " + json);
        }

        String operator = JsonHelper.getValue(JsonHelper.getObject(ops, 0), "_type");
        var leftObject = JsonHelper.getObject(json, "left");
        var rightObject = JsonHelper.getObject(comparators, 0);
        Expression left = factory.build(leftObject, UnknownExpression.DEFAULT);
        Expression right = factory.build(rightObject, UnknownExpression.DEFAULT);

        return new BinaryOperator(left, right, operator);
    }
}
