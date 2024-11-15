package cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers;

import cn.edu.engine.qvog.engine.helper.JsonHelper;
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
    //函数getType的作用就是从json中找到_type对应的值。

    //这里要约定好字段名是_type
    //至于这个type之后要怎么用目前还不清楚
}
