package cn.edu.engine.qvog.engine.dsl.fluent.filter;

import cn.edu.engine.qvog.engine.dsl.data.ITable;

public class FilterPredicate implements IFilterPredicate {
    private final String name;
    private final IRowPredicate predicate;

    FilterPredicate(String name, IRowPredicate predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public String getTableName() {
        return name;
    }

    @Override
    public ITable filter(ITable table) {
        var newTable = table.duplicate();
        var it = table.iteratorWithHeader();
        while (it.hasNext()) {
            var row = it.next();
            if (predicate.test(row)) {
                newTable.addRow(row);
            }
        }
        return newTable;
    }
}
