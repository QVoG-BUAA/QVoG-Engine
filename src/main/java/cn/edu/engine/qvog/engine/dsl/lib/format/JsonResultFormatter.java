package cn.edu.engine.qvog.engine.dsl.lib.format;

import cn.edu.engine.qvog.engine.dsl.lib.engine.QueryResult;
import org.apache.commons.text.StringEscapeUtils;

public class JsonResultFormatter implements IResultFormatter {
    private boolean minified;

    public JsonResultFormatter minified() {
        minified = true;
        return this;
    }

    @Override
    public String format(QueryResult result) {
        return "{" + (minified ? "" : "\n\t") +
                "\"name\":" + (minified ? "" : " ") +
                '"' + result.name() + '"' +
                (minified ? "," : ",\n\t") +
                "\"result\":" + (minified ? "" : " ") +
                '"' + StringEscapeUtils.escapeJson(result.result()) + '"' +
                (minified ? "," : ",\n\t") +
                "\"milliseconds\":" + (minified ? "" : " ") +
                result.milliseconds() + (minified ? "}" : "\n}");
    }

    @Override
    public String formatTotalTime(long milliseconds) {
        return "Total execution time: " + milliseconds;
    }
}
