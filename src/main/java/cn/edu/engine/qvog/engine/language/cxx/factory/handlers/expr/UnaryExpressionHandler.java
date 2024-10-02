package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnaryOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class UnaryExpressionHandler extends BaseHandler implements IValueHandler<UnaryOperator> {
    @Override
    public UnaryOperator build(JSONObject json, IValueFactory factory) {
        Expression operand = factory.build(JsonHelper.getObject(json, "operand"), UnknownExpression.DEFAULT);
        String operator = JsonHelper.getValue(json, "operator");

        return new UnaryOperator(operand, operator);
    }
}
