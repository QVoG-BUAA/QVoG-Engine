package cn.edu.buaa.qvog.engine.language.python.factory.handlers;

import cn.edu.buaa.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.buaa.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.buaa.qvog.engine.core.graph.values.UnknownValue;
import cn.edu.buaa.qvog.engine.core.graph.values.Value;
import cn.edu.buaa.qvog.engine.core.graph.values.statements.ReturnStatement;
import cn.edu.buaa.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

public class ReturnStatementHandler extends BaseHandler implements IValueHandler<ReturnStatement> {
    @Override
    public ReturnStatement build(JSONObject json, IValueFactory factory) {
        Value value = factory.build(
                JsonHelper.getObject(json, "value"),
                UnknownValue.DEFAULT);
        return new ReturnStatement(value);
    }
}
