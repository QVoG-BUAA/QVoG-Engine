package cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.TypeCastExpression;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class TypeCastExpressionHandler extends BaseHandler implements IValueHandler<TypeCastExpression> {
    @Override
    public TypeCastExpression build(JSONObject json, IValueFactory factory) {
        Expression operand = factory.build(
                JsonHelper.getObject(json, "operand"),
                UnknownExpression.DEFAULT);

        // FIXME implement type
        return new TypeCastExpression(operand, "typeCast");
    }
}