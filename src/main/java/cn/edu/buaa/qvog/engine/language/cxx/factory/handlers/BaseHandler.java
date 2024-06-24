package cn.edu.buaa.qvog.engine.language.cxx.factory.handlers;

import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public abstract class BaseHandler {
    protected static String getOpType(JSONObject json) {
        return getSubType(json, "operator");
    }

    protected static String getSubType(JSONObject json, String key) {
        return JsonHelper.getValue(JsonHelper.getObject(json, key), "_type");
    }

    protected static String getType(JSONObject json) {
        return JsonHelper.getValue(json, "_type");
    }
}
