package cn.edu.engine.qvog.engine.language.cxx.factory.handlers.stmt;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.DeclarationStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownReference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.language.cxx.factory.handlers.BaseHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DeclarationStatementHandler extends BaseHandler implements IValueHandler<DeclarationStatement> {
    @Override
    public DeclarationStatement build(JSONObject json, IValueFactory factory) {
        JSONArray targets = JsonHelper.getArray(json, "targets");
        ArrayList<Reference> references = new ArrayList<>();
        ArrayList<Expression> values = new ArrayList<>();
        for (Object target : targets) {
            JSONObject jsonObject = (JSONObject) target;
            references.add(factory.build(jsonObject, UnknownReference.DEFAULT));

            JSONObject value = JsonHelper.tryGetObject(jsonObject, "value");
            if (value != null) {
                values.add(factory.build(value, UnknownExpression.DEFAULT));
            }
        }
        return new DeclarationStatement(references, values);
    }
}
