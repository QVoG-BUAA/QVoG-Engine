package cn.edu.engine.qvog.engine.language.cxx.lib;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.graph.values.statements.expressions.CallExpression;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class ContainsMysqlUseFunction implements IValuePredicate {
    @Override
    public boolean test(Value value) {
        return value.toStream().anyMatch(v -> v instanceof CallExpression callExpression &&
                ("mysql_query".equals(callExpression.getFunction().getName())
                        || "mysql_real_query".equals(callExpression.getFunction().getName())
                        || "mysql_store_result".equals(callExpression.getFunction().getName())
                        || "mysql_use_result".equals(callExpression.getFunction().getName())
                        || "mysql_ping".equals(callExpression.getFunction().getName())
                        || "mysql_commit".equals(callExpression.getFunction().getName())
                        || "mysql_rollback".equals(callExpression.getFunction().getName())
                        || "mysql_autocommit".equals(callExpression.getFunction().getName())
                ));
    }
}
