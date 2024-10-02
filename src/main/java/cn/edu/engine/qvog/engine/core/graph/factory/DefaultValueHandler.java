package cn.edu.engine.qvog.engine.core.graph.factory;

import cn.edu.engine.qvog.engine.core.graph.values.UnknownValue;
import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.json.simple.JSONObject;

public class DefaultValueHandler implements IValueHandler<Value> {
    @Override
    public Value build(JSONObject json, IValueFactory factory) {
        return UnknownValue.DEFAULT;
    }
}
