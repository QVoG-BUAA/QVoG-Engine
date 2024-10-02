package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.ReturnStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class ReturnStatementHandler extends BaseHandler implements IValueHandler<ReturnStatement> {
    @Override
    public ReturnStatement build(JSONObject json, IValueFactory factory) {
        JSONObject value = JsonHelper.getObject(json, "value");
        return new ReturnStatement(factory.build(value, UnknownExpression.DEFAULT));
    }
}
