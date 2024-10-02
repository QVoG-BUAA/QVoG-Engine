package cn.edu.engine.qvog.engine.language.java.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.*;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

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

        return builder.build();
    }
}
