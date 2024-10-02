package cn.edu.engine.qvog.engine.dsl.data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderedTable extends BaseTable {
    private final ArrayList<IColumn> columns = new ArrayList<>();

    private OrderedTable(String name) {
        super(name);
    }

    @Override
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public Collection<Object> getRow(int row) {
        ArrayList<Object> values = new ArrayList<>();
        columns.forEach(column -> values.add(column.getValue(row)));
        return values;
    }

    @Override
    public boolean hasColumn(String name) {
        for (var column : columns) {
            if (column.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasColumn(IColumn column) {
        return columns.contains(column);
    }

    @Override
    public IColumn getColumn(String name) {
        for (var column : columns) {
            if (column.name().equals(name)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Column with name " + name + " does not exist");
    }

    @Override
    public Collection<String> getColumnNames() {
        return columns.stream().map(IColumn::name).collect(Collectors.toList());
    }

    @Override
    public Set<Map.Entry<String, IColumn>> getColumnEntries() {
        // convert all entries to a set
        var entries = new HashSet<Map.Entry<String, IColumn>>();
        columns.forEach(column -> entries.add(new AbstractMap.SimpleEntry<>(column.name(), column)));
        return entries;
    }

    @Override
    public ITable duplicate(boolean schemaOnly) {
        var table = OrderedTable.builder().withName(name);
        for (var column : columns) {
            table.withColumn(column.duplicate(schemaOnly));
        }
        return table.build();
    }

    @Override
    public IColumn asColumn() {
        if (columns.size() != 1) {
            throw new IllegalStateException("Table has no or more than one column");
        }
        return columns.get(0);
    }

    @Override
    public void amend(String newName) {
        var oldName = asColumn().name();
        var column = removeColumn(oldName);
        column.amend(newName);
        addColumn(column);
        name = newName;
    }

    public static Builder builder() {
        return new OrderedTableBuilder();
    }

    @Override
    protected Collection<IColumn> _getColumns() {
        return columns;
    }

    @Override
    protected IColumn _removeColumn(String name) {
        for (var column : columns) {
            if (column.name().equals(name)) {
                columns.remove(column);
                return column;
            }
        }
        throw new IllegalArgumentException("Column with name " + name + " does not exist");
    }

    @Override
    protected IColumn _addColumn(IColumn column) {
        if (hasColumn(column.name())) {
            throw new IllegalArgumentException("Column with name " + column.name() + " already exists");
        }
        columns.add(column);
        return column;
    }

    public static class OrderedTableBuilder implements Builder {
        private OrderedTable table;

        @Override
        public Builder withName(String name) {
            table = new OrderedTable(name);
            return this;
        }

        @Override
        public Builder withColumn(Function<IColumn.Builder, IColumn.Builder> builder) {
            if (table == null) {
                throw new IllegalStateException("Table name not set");
            }
            var column = builder.apply(DataColumn.builder()).build();
            table.addColumn(column);
            return this;
        }

        @Override
        public Builder withColumn(IColumn column) {
            if (table == null) {
                throw new IllegalStateException("Table name not set");
            }
            table.addColumn(column);
            return this;
        }

        @Override
        public ITable build() {
            if (table == null) {
                throw new IllegalStateException("Table name not set");
            }
            return table;
        }
    }
}
