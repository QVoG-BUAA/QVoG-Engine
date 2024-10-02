package cn.edu.engine.qvog.engine.dsl.fluent.filter;

public interface IFilterPredicateDescriptor extends ICanSetFilterPredicate {
    ICanSetFilterPredicate onTable(String name);
}
