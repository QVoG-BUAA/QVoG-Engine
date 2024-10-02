package cn.edu.engine.qvog.engine.language.java.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class ReferenceHandler extends BaseHandler implements IValueHandler<Reference> {
    @Override
    public Reference build(JSONObject json, IValueFactory factory) {
//        String type = JsonHelper.getValue(json, "type");
        String name = JsonHelper.getValue(json, "id");

        // TODO: Add type to Reference.

        return new Reference(name, Reference.ReferenceTypes.Unspecified);
    }
}
