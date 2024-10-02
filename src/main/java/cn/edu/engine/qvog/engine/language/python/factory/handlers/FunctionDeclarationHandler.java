package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.declarations.FunctionDeclaration;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class FunctionDeclarationHandler extends BaseHandler implements IValueHandler<FunctionDeclaration> {
    @Override
    public FunctionDeclaration build(JSONObject json, IValueFactory factory) {
        String name = JsonHelper.getValue(json, "name");
        return new FunctionDeclaration(name);
    }
}
