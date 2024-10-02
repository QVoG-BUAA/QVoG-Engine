package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.types.BoolType;
import cn.edu.engine.qvog.engine.core.graph.types.FloatType;
import cn.edu.engine.qvog.engine.core.graph.types.IntegerType;
import cn.edu.engine.qvog.engine.core.graph.types.StringType;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Literal;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

/**
 * Handler for {@link Literal}.
 */
public class LiteralHandler extends BaseHandler implements IValueHandler<Literal> {
    @Override
    public Literal build(JSONObject json, IValueFactory factory) {
        Object valueObject = JsonHelper.get(json, "value");
        if (valueObject instanceof Integer value) {
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
