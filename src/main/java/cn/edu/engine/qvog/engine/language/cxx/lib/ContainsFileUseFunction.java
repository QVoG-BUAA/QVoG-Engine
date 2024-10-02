package cn.edu.engine.qvog.engine.language.cxx.lib;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsFileUseFunction implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return value.toStream().anyMatch(v -> v instanceof CallExpression callExpression &&
                ("fread".equals(callExpression.getFunction().getName())
                        || "fwrite".equals(callExpression.getFunction().getName())
                        || "fprintf".equals(callExpression.getFunction().getName())
                        || "fscanf".equals(callExpression.getFunction().getName())
                        || "fgets".equals(callExpression.getFunction().getName())
                        || "fputs".equals(callExpression.getFunction().getName())
                        || "fgetc".equals(callExpression.getFunction().getName())
                        || "fputc".equals(callExpression.getFunction().getName())
                ));
    }
}
