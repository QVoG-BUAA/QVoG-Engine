package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.helper.TypeHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

/**
 * idExpr
 */
public class IdExpressionHandler extends BaseHandler implements IValueHandler<Reference> {
    @Override
    public Reference build(JSONObject json, IValueFactory factory) {
        String name = JsonHelper.getValue(json, "id");
        String value = JsonHelper.tryGetValue(json, "role");
        JSONObject typeObject = JsonHelper.tryGetObject(json, "type");
        if (typeObject != null) {
            String value1 = JsonHelper.tryGetValue(typeObject, "name");
            if (value != null && value1 != null) {
                Reference.ReferenceTypes refType = switch (value) {
                    case "DECLARATION", "DEFINITION" -> Reference.ReferenceTypes.Left;
                    case "REFERENCE" -> Reference.ReferenceTypes.Right;
                    default -> Reference.ReferenceTypes.Unspecified;
                };
                Type type = TypeHelper.buildCxxType(typeObject);
                return new Reference(type, name, refType);
            }
        }
        return new Reference(name);
    }
}
