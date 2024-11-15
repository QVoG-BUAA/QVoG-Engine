package cn.edu.engine.qvog.engine.language.ArkTS.factory.handlers.expr;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.ArkTS.ArkTSDeleteExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class DeleteExpressionHandler extends BaseHandler implements IValueHandler<ArkTSDeleteExpression> {
    @Override
    public ArkTSDeleteExpression build(JSONObject json, IValueFactory factory) {
        Boolean isVectored = (Boolean) JsonHelper.get(json, "isVectored");
        JSONObject target = JsonHelper.getObject(json, "target");
        return new ArkTSDeleteExpression(factory.build(target, UnknownExpression.DEFAULT), isVectored);
    }
}
