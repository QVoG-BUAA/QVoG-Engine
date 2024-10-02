package cn.edu.engine.qvog.engine.dsl.lib.predicate;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.IRowPredicate;


public interface IValuePredicate {
    default IRowPredicate onColumn(String column) {
        return (row) -> {
            if (row.get(column) instanceof Value value) {
                return test(value);
            }
            throw new IllegalArgumentException("Column " + column + " is not a Value");
        };
    }

    boolean test(Value value);

    default IValuePredicate negate() {
        return (v) -> !test(v);
    }

    default IValuePredicate and(IValuePredicate other) {
        return (v) -> test(v) && other.test(v);
    }

    default IValuePredicate or(IValuePredicate other) {
        return (v) -> test(v) || other.test(v);
    }
}
