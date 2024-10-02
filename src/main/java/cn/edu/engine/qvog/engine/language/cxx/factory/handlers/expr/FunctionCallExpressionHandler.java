package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownReference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

/**
 * Handler for {@link CallExpression}.
 */
public class FunctionCallExpressionHandler extends BaseHandler implements IValueHandler<CallExpression> {
    @Override
    public CallExpression build(JSONObject json, IValueFactory factory) {
        var builder = CallExpression.builder();

        Reference function = factory.build(
                JsonHelper.getObject(json, "func"),
                UnknownReference.DEFAULT);
        builder.function(function);

        JsonHelper.getObjectElements(JsonHelper.getArray(json, "args"))
                .forEach(arg -> builder.addArgument(
                        factory.build(
                                arg,
                                UnknownExpression.DEFAULT)));
        return builder.build();
    }
}
