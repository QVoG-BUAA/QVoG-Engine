package cn.edu.engine.qvog.engine.language.python.lib.helper;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.constrains.HasName;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.MemberExpression;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeHelper {
    private AttributeHelper() {}

    public static String extractAttributes(Value value, String delimiter) {
        return String.join(delimiter, extractAttributes(value));
    }

    public static List<String> extractAttributes(Value value) {
        if (value instanceof MemberExpression member) {
            return extractMemberExpressionAttributes(member);
        } else if (value instanceof Reference reference) {
            return new ArrayList<>(List.of(reference.getName()));
        } else if (value instanceof HasName namedValue) {
            return new ArrayList<>(List.of(namedValue.getName()));
        } else {
            throw new IllegalArgumentException("Unable to get attributes of " + value);
        }
    }

    private static List<String> extractMemberExpressionAttributes(MemberExpression member) {
        ArrayList<String> attrs = new ArrayList<>();
        attrs.add(member.getName());
        while (member.getBase() instanceof MemberExpression base) {
            attrs.add(base.getName());
            member = base;
        }
        if (member.getBase() instanceof HasName base) {
            attrs.add(base.getName());
        }
        Collections.reverse(attrs);
        return attrs;
    }


}
