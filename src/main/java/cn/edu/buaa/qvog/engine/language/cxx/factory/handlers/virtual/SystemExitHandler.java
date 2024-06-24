package cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.virtual;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.cxx.virtuals.CxxSystemExit;
import cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class SystemExitHandler extends BaseHandler implements IValueHandler<CxxSystemExit> {
    @Override
    public CxxSystemExit build(JSONObject json, IValueFactory factory) {
        return new CxxSystemExit(json);
    }
}
