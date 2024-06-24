package cn.edu.buaa.qvog.engine.dsl.fluent.filter;

public interface IFilterPredicateDescriptor extends ICanSetFilterPredicate {
    ICanSetFilterPredicate onTable(String name);
}
