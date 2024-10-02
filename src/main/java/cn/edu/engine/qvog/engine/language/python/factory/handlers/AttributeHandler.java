package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.MemberExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * Member access is called Attribute in Python.
 */
public class AttributeHandler extends BaseHandler implements IValueHandler<MemberExpression> {
    @Override
    public MemberExpression build(JSONObject json, IValueFactory factory) {
        var refType = getReferenceType(json);
        String name = JsonHelper.getValue(json, "attr");
        Expression base = factory.build(
                JsonHelper.getObject(json, "value"),
                UnknownExpression.DEFAULT);

        return new MemberExpression(name, refType, base);
    }
}
