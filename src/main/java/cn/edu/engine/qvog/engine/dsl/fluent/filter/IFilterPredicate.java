package cn.edu.engine.qvog.engine.dsl.fluent.filter;

import cn.edu.engine.qvog.engine.dsl.data.ITable;

public interface IFilterPredicate {
    /**
     * Which table this predicate is for. If null, there must
     * be only one table in the query.
     *
     * @return The table name.
     */
    String getTableName();

    /**
     * Filter the table based on this predicate.
     * Note that the returned table is a <b>NEW</b> table, which means that
     * the original table should not be modified.
     *
     * @param table The table to filter.
     * @return The filtered table.
     */
    ITable filter(ITable table);
}
