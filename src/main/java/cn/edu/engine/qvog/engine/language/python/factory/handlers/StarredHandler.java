package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class StarredHandler extends BaseHandler implements IValueHandler<Reference> {
    @Override
    public Reference build(JSONObject json, IValueFactory factory) {
        JSONObject value = JsonHelper.getObject(json, "value");
        Reference.ReferenceTypes refType = getReferenceType(value);
        String name = JsonHelper.getValue(value, "id");

        return new Reference(name, refType);
    }
}
