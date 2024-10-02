package cn.edu.engine.qvog.engine.language.java.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.types.BoolType;
import cn.edu.engine.qvog.engine.core.graph.types.FloatType;
import cn.edu.engine.qvog.engine.core.graph.types.IntegerType;
import cn.edu.engine.qvog.engine.core.graph.types.StringType;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Literal;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class LiteralValueHandler extends BaseHandler implements IValueHandler<Literal> {
    @Override
    public Literal build(JSONObject json, IValueFactory factory) {
        Object valueObject = JsonHelper.get(json, "value");
        Boolean isNull = (Boolean) JsonHelper.get(json, "isNull");

        if (isNull) {
            return new Literal(null);
        } else if (valueObject instanceof Integer value) {
            return new Literal(IntegerType.UNSPECIFIED, value);
        } else if (valueObject instanceof Long value) {
            return new Literal(IntegerType.UNSPECIFIED, value);
        } else if (valueObject instanceof Float value) {
            return new Literal(FloatType.UNSPECIFIED, value);
        } else if (valueObject instanceof Boolean value) {
            return new Literal(BoolType.DEFAULT, value);
        } else if (valueObject instanceof String value) {
            return new Literal(StringType.DEFAULT, value);
        } else {
            throw new RuntimeException("Unknown literal value type: " + valueObject.getClass().getName());
        }
    }
}
