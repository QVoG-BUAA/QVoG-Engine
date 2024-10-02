package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.*;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * Handler for {@link CallExpression}.
 */
public class CallHandler extends BaseHandler implements IValueHandler<CallExpression> {
    @Override
    public CallExpression build(JSONObject json, IValueFactory factory) {
        var builder = CallExpression.builder();

        Reference function = factory.build(
                JsonHelper.getObject(json, "func"),
                UnknownReference.DEFAULT);
        builder.function(function);
        JsonHelper.getObjectElements(JsonHelper.getArray(json, "args"))
                .forEach(arg -> builder.addArgument(
                        factory.<Expression, UnknownExpression>build(
                                arg,
                                UnknownExpression.DEFAULT)));

        // Keyword arguments is treated as normal arguments.
        JsonHelper.getObjectElements(JsonHelper.getArray(json, "keywords"))
                .forEach(keyword -> builder.addArgument(
                        factory.<Expression, UnknownExpression>build(
                                keyword,
                                UnknownExpression.DEFAULT)));

        return builder.build();
    }
}
