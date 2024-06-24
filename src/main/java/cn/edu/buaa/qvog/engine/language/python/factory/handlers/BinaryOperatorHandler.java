package cn.edu.buaa.qvog.engine.language.python.factory.handlers;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.BinaryOperator;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class BinaryOperatorHandler extends BaseHandler implements IValueHandler<BinaryOperator> {
    @Override
    public BinaryOperator build(JSONObject json, IValueFactory factory) {
        Expression left = factory.build(JsonHelper.getObject(json, "left"), UnknownExpression.DEFAULT);
        Expression right = factory.build(JsonHelper.getObject(json, "right"), UnknownExpression.DEFAULT);
        String operator = getOpType(json);

        return new BinaryOperator(left, right, operator);
    }
}
