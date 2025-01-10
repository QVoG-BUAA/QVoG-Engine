package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArkTS.virtuals;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import org.json.simple.JSONObject;

public class ArkTSSystemExit extends Value {
    private final JSONObject jsonObject;

    public ArkTSSystemExit(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
