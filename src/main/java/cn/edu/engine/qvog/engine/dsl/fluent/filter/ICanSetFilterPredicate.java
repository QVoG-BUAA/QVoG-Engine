package cn.edu.engine.qvog.engine.dsl.fluent.filter;

import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public interface ICanSetFilterPredicate {
    ICanBuildFilterPredicate where(IRowPredicate predicate);

    ICanBuildFilterPredicate where(IValuePredicate predicate);
}
