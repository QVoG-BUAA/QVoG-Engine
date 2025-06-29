package cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArkTS.ArkTSNewExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownReference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class NewExpressionHandler extends BaseHandler implements IValueHandler<ArkTSNewExpression> {
    @Override
    public ArkTSNewExpression build(JSONObject jsonObject, IValueFactory factory) {
        // TODO
        String valueType = JsonHelper.getValue(jsonObject, "type");

        JSONArray subs = JsonHelper.tryGetArray(jsonObject, "subs");
        ArrayList<Reference> references = new ArrayList<>();
        if (subs != null) {
            for (Object sub : subs) {
                references.add(factory.build(((JSONObject) sub), UnknownReference.DEFAULT));
            }
        }

        Boolean isVectored = (Boolean) JsonHelper.get(jsonObject, "isVectored");
        return new ArkTSNewExpression(references, isVectored);
        //这些基本上还都是把json中取到的对象封装到底层的数据结构中
    }
}
