package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public abstract class BaseHandler {
    protected static String getType(JSONObject json) {
        return JsonHelper.getValue(json, "_type");
    }

    protected static String getOpType(JSONObject json) {
        return getSubType(json, "op");
    }

    protected static String getSubType(JSONObject json, String key) {
        return JsonHelper.getValue(JsonHelper.getObject(json, key), "_type");
    }

    protected static Reference.ReferenceTypes getReferenceType(JSONObject json) {
        return switch (getCtxType(json)) {
            case "Load" -> Reference.ReferenceTypes.Right;
            case "Store" -> Reference.ReferenceTypes.Left;
            default -> Reference.ReferenceTypes.Unspecified;
        };
    }

    protected static String getCtxType(JSONObject json) {
        return getSubType(json, "ctx");
    }
}
