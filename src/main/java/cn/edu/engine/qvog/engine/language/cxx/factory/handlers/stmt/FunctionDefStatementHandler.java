package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.FunctionDefStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownReference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FunctionDefStatementHandler extends BaseHandler implements IValueHandler<FunctionDefStatement> {
    @Override
    public FunctionDefStatement build(JSONObject json, IValueFactory factory) {
        JSONObject func = JsonHelper.getObject(json, "func");
        List<JSONObject> args = JsonHelper.getObjectElements(JsonHelper.getArray(json, "args"));
        ArrayList<Reference> references = new ArrayList<>();
        for (JSONObject arg : args) {
            references.add(factory.build(arg, UnknownReference.DEFAULT));
        }
        return new FunctionDefStatement(
                factory.build(func, UnknownReference.DEFAULT),
                references
        );
    }
}
