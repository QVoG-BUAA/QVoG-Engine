package cn.edu.buaa.qvog.engine.language.java.factory.handlers;

import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public abstract class BaseHandler {
    protected static String getType(JSONObject json) {
        return JsonHelper.getValue(json, "_type");
    }
}
