package cn.edu.engine.qvog.engine.helper;

import cn.edu.engine.qvog.engine.core.graph.types.PointerType;
import cn.edu.engine.qvog.engine.core.graph.types.Type;
import cn.edu.engine.qvog.engine.core.graph.types.UnknownType;
import org.json.simple.JSONObject;

public class TypeHelper {
    public static Type buildCxxType(JSONObject json) {
        Object object = json.get("type");
        if (object instanceof JSONObject) {
            return UnknownType.DEFAULT;
        }
        String type = JsonHelper.getValue(json, "type");
        String name = JsonHelper.getValue(json, "name");
        if ("basic".equals(type) || "struct".equals(type) || "union".equals(type)) {
            // TODO need to parse it by literal
            return UnknownType.DEFAULT;
        } else if ("pointer".equals(type) || "array".equals(type) || "function".equals(type)) {
            return new PointerType(name);
        }
        return UnknownType.DEFAULT;
    }
}
