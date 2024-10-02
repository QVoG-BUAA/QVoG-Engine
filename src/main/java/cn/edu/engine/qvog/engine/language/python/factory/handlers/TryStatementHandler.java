package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.TryStatement;
import org.json.simple.JSONObject;

public class TryStatementHandler extends BaseHandler implements IValueHandler<TryStatement> {
    @Override
    public TryStatement build(JSONObject json, IValueFactory factory) {
        return new TryStatement();
    }
}
