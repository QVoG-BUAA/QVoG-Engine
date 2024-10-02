package cn.edu.engine.qvog.engine.dsl.lib.format;

import com.sarojaba.prettytable4j.Converter;
import com.sarojaba.prettytable4j.PrettyTable;
import org.apache.commons.text.StringEscapeUtils;

public class JsonConverter implements Converter {
    private boolean minified;

    public JsonConverter minified() {
        minified = true;
        return this;
    }

    @Override
    public String convert(PrettyTable pt) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(minified ? "" : "\n\t");
        sb.append("\"headers\":");
        sb.append(minified ? "[" : "[\n\t\t");

        for (int i = 0; i < pt.fieldNames.size(); ++i) {
            if (i > 0) {
                sb.append(minified ? "," : ", ");
            }
            sb.append('"').append(pt.fieldNames.get(i)).append('"');
        }
        sb.append(minified ? "]," : "\n\t],\n\t");

        sb.append("\"rows\":");
        sb.append(minified ? "[" : "[\n\t\t");

        var it = pt.rows.iterator();
        int i = 0;
        while (it.hasNext()) {
            var row = it.next();
            sb.append("[");
            for (int c = 0; c < row.length; ++c) {
                if (c > 0) {
                    sb.append(minified ? "," : ", ");
                }
                sb.append('\"');
                sb.append(StringEscapeUtils.escapeJson(row[c].toString()));
                sb.append('\"');
            }
            sb.append("]");
            if (i < pt.rows.size() - 1) {
                sb.append(minified ? "," : ",\n\t\t");
            } else {
                sb.append(minified ? "" : "\n\t");
            }
            i++;
        }

        sb.append(minified ? "]}" : "]\n}");

        return sb.toString();
    }
}
