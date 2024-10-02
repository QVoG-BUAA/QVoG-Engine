package cn.edu.engine.qvog.engine.dsl.fluent.filter;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public class FilterPredicateDescriptor implements
        IFilterPredicateDescriptor, ICanSetFilterPredicate, ICanBuildFilterPredicate {

    private String tableName;
    private IRowPredicate predicate;

    private FilterPredicateDescriptor() {
    }

    public static IFilterPredicateDescriptor create() {
        return new FilterPredicateDescriptor();
    }

    @Override
    public IFilterPredicate build() {
        return new FilterPredicate(tableName, predicate);
    }

    @Override
    public ICanBuildFilterPredicate where(IRowPredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public ICanBuildFilterPredicate where(IValuePredicate predicate) {
        if (tableName == null) {
            throw new IllegalStateException("Set tableName before setting a Value predicate");
        }
        this.predicate = row -> predicate.test((Value) row.get(tableName));
        return this;
    }

    @Override
    public ICanSetFilterPredicate onTable(String name) {
        this.tableName = name;
        return this;
    }
}
