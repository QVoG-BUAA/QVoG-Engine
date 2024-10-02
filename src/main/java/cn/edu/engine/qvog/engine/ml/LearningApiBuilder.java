package cn.edu.engine.qvog.engine.ml;

import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class LearningApiBuilder {
    private LearningApiBuilder() {}

    public static ILearningApi connect(JSONObject config) {
        if (config == null) {
            return new DummyLearningApi();
        }

        var enabled = JsonHelper.getBoolValue(config, "enabled");
        if (!enabled) {
            return new DummyLearningApi();
        }

        var baseUrl = JsonHelper.tryGetValue(config, "baseUrl");
        if (baseUrl != null) {
            return new LearningApi(baseUrl);
        }

        var host = JsonHelper.getValue(config, "host");
        var port = JsonHelper.getIntValue(config, "port");
        return new LearningApi(host, port);
    }
}
