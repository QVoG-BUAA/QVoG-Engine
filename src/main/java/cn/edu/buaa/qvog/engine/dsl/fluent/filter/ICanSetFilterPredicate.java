package cn.edu.buaa.qvog.engine.dsl.fluent.filter;

import cn.edu.buaa.qvog.engine.dsl.lib.predicate.IValuePredicate;

public interface ICanSetFilterPredicate {
    ICanBuildFilterPredicate where(IRowPredicate predicate);

    ICanBuildFilterPredicate where(IValuePredicate predicate);
}
