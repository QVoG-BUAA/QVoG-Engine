package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArraySubExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ArraySubExpressionHandler extends BaseHandler implements IValueHandler<ArraySubExpression> {
    @Override
    public ArraySubExpression build(JSONObject jsonObject, IValueFactory factory) {
        Expression array = factory.build(JsonHelper.getObject(jsonObject, "arr"));

        JSONArray subsJson = JsonHelper.getArray(jsonObject, "subs");
        ArrayList<Expression> subs = new ArrayList<>();
        for (Object sub : subsJson) {
            subs.add(factory.build((JSONObject) sub));
        }
        return new ArraySubExpression(subs, array);
    }
}
