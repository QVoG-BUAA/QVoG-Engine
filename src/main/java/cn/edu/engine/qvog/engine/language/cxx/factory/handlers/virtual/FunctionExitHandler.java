package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.virtual;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxFunctionExit;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class FunctionExitHandler extends BaseHandler implements IValueHandler<CxxFunctionExit> {
    @Override
    public CxxFunctionExit build(JSONObject json, IValueFactory factory) {
        return new CxxFunctionExit(json);
    }
}
