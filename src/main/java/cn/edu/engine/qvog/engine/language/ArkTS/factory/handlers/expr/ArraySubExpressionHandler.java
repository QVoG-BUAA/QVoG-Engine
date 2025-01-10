package cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArraySubExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class ArraySubExpressionHandler extends BaseHandler implements IValueHandler<ArraySubExpression> {

    @Override
    public ArraySubExpression build(JSONObject jsonObject, IValueFactory factory) {
        Expression array = factory.build(JsonHelper.getObject(jsonObject, "arr"));
        //获取当前jsonObject中arr对应的jsonObject并传给factory,再由其中的handles处理
        JSONArray subsJson = JsonHelper.getArray(jsonObject, "subs");
        //虽然不知道这个subsJson到底代表的是什么，但是模仿着写上吧
        ArrayList<Expression> subs = new ArrayList<>();
        for (Object sub : subsJson) {
            subs.add(factory.build((JSONObject) sub));
        }
        return new ArraySubExpression(subs, array);
        //返回值构造了一个ArraySubExpression，将这两个融合在了一起。
        // 然后又设置了一个Type（TupleType本质上是一个Type数组）
        //还设置了parent
    }
    //很奇怪这里的json是怎么来的
}
