package cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.stmt;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.ReturnStatement;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import cn.edu.buaa.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONObject;

public class ReturnStatementHandler extends BaseHandler implements IValueHandler<ReturnStatement> {
    @Override
    public ReturnStatement build(JSONObject json, IValueFactory factory) {
        JSONObject value = JsonHelper.getObject(json, "value");
        return new ReturnStatement(factory.build(value, UnknownExpression.DEFAULT));
    }
}
