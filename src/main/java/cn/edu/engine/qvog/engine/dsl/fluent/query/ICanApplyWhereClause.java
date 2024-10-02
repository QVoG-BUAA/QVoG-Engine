package cn.edu.engine.qvog.engine.dsl.fluent.query;

import cn.edu.engine.qvog.engine.dsl.fluent.filter.FilterPredicateDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.ICanBuildFilterPredicate;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.IFilterPredicate;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.IFilterPredicateDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.IFlowPredicate;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

import java.util.function.Function;

/**
 * Filter data in one table.
 * Table -> Table
 */
public interface ICanApplyWhereClause {
    /**
     * Filter table data by predicate. This is used when a custom
     * predicate is build on the fly.
     *
     * @param descriptor Descriptor to build predicate.
     * @return {@link FilteredQuery}.
     */
    default FilteredQuery where(Function<IFilterPredicateDescriptor, ICanBuildFilterPredicate> descriptor) {
        return where(descriptor.apply(FilterPredicateDescriptor.create()).build());
    }

    /**
     * Filter table data by predicate. This is used when we only have
     * one table.
     *
     * @param predicate Row Predicate.
     * @return {@link FilteredQuery}.
     */
    FilteredQuery where(IFilterPredicate predicate);

    /**
     * If the current query has exactly one table and the table has exactly one column,
     * this method can be used to filter the data in the table.
     * Suffix P to indicate that the predicate is a value predicate to avoid
     * ambiguity with the previous method {@link #where(IFilterPredicate)}.
     *
     * @param predicate Predicate.
     * @return {@link FilteredQuery}.
     */
    FilteredQuery whereP(IValuePredicate predicate);


    /**
     * This is a join operation. It takes all tables in the current query,
     * and returns a new joined table.
     * Note that the clause <b>MUST</b> manually remove old tables, and
     * return a new table.
     *
     * @param clause Join clause.
     * @return {@link FilteredQuery}.
     */
    FilteredQuery where(IFlowPredicate clause);
}
