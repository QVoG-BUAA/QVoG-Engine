package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnaryOperator;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class UnaryOperatorHandler extends BaseHandler implements IValueHandler<UnaryOperator> {
    @Override
    public UnaryOperator build(JSONObject json, IValueFactory factory) {
        Expression operand = factory.build(JsonHelper.getObject(json, "operand"), UnknownExpression.DEFAULT);
        String operator = getOpType(json);

        return new UnaryOperator(operand, operator);
    }
}
