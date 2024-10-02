package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.virtual;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxSystemExit;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class SystemExitHandler extends BaseHandler implements IValueHandler<CxxSystemExit> {
    @Override
    public CxxSystemExit build(JSONObject json, IValueFactory factory) {
        return new CxxSystemExit(json);
    }
}
