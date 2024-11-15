package cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.BinaryOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class BinaryExpressionHandler extends BaseHandler implements IValueHandler<BinaryOperator> {
    @Override
    public BinaryOperator build(JSONObject json, IValueFactory factory) {
        Expression left = factory.build(JsonHelper.getObject(json, "leftOp"), UnknownExpression.DEFAULT);
        Expression right = factory.build(JsonHelper.getObject(json, "rightOp"), UnknownExpression.DEFAULT);
        String operator = JsonHelper.getValue(json, "operator");
        //感觉都不知道该怎么修改
        //这里主要还是要和传入的json中的字段去匹配
        return new BinaryOperator(left, right, operator);
    }
}
