package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * Take care of the reference node, which has the type of "Name".
 */
public class ReferenceHandler extends BaseHandler implements IValueHandler<Reference> {

    @Override
    public Reference build(JSONObject json, IValueFactory factory) {
        Reference.ReferenceTypes refType = getReferenceType(json);
        String name = JsonHelper.getValue(json, "id");

        return new Reference(name, refType);
    }
}
