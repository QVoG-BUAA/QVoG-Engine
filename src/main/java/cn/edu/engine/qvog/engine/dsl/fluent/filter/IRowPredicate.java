package cn.edu.engine.qvog.engine.dsl.fluent.filter;

import java.util.Map;

public interface IRowPredicate {
    default IRowPredicate and(IRowPredicate other) {
        return row -> test(row) && other.test(row);
    }

    boolean test(Map<String, Object> row);

    default IRowPredicate or(IRowPredicate other) {
        return row -> test(row) || other.test(row);
    }

    default IRowPredicate negate() {
        return row -> !test(row);
    }
}
