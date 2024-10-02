package cn.edu.engine.qvog.engine.language.java.factory.handlers;

import cn.edu.engine.qvog.engine.core.graph.factory.IValueFactory;
import cn.edu.engine.qvog.engine.core.graph.factory.IValueHandler;
import cn.edu.engine.qvog.engine.core.graph.values.statements.DeclarationStatement;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Expression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.UnknownReference;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeclStatementHandler extends BaseHandler implements IValueHandler<DeclarationStatement> {
    @Override
    public DeclarationStatement build(JSONObject json, IValueFactory factory) {
        var targetObjects = JsonHelper.getObjectElements(JsonHelper.getArray(json, "targets"));

        List<Reference> targets = targetObjects.stream().map(target -> factory.<Reference, UnknownReference>build(target, UnknownReference.DEFAULT)).toList();
        var valueObject = JsonHelper.tryGetObject(json, "value");
        Expression value = null;
        if (valueObject != null) {
            value = factory.<Expression, UnknownExpression>build(valueObject, UnknownExpression.DEFAULT);
        }
        return new DeclarationStatement(targets, value == null ? new ArrayList<>() : List.of(value));
    }
}
