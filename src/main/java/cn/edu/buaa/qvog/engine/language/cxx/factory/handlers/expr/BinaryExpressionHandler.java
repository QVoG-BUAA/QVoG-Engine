package cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.BinaryOperator;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class BinaryExpressionHandler extends BaseHandler implements IValueHandler<BinaryOperator> {
    @Override
    public BinaryOperator build(JSONObject json, IValueFactory factory) {
        Expression left = factory.build(JsonHelper.getObject(json, "leftOp"), UnknownExpression.DEFAULT);
        Expression right = factory.build(JsonHelper.getObject(json, "rightOp"), UnknownExpression.DEFAULT);
        String operator = JsonHelper.getValue(json, "operator");

        return new BinaryOperator(left, right, operator);
    }
}