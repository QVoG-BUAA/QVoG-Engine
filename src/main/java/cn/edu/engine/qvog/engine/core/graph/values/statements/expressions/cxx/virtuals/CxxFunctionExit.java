package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.json.simple.JSONObject;

public class CxxFunctionExit extends Value {
    private final JSONObject jsonObject;

    public CxxFunctionExit(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}