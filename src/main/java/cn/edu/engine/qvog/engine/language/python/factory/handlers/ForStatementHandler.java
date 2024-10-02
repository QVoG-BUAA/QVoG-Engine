package cn.edu.engine.qvog.engine.language.python.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.ForStatement;
import org.json.simple.JSONObject;

public class ForStatementHandler extends BaseHandler implements IValueHandler<ForStatement> {
    @Override
    public ForStatement build(JSONObject json, IValueFactory factory) {
        return new ForStatement();
    }
}
