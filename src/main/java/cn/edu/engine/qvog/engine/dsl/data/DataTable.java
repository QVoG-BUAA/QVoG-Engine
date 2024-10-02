package cn.edu.engine.qvog.engine.dsl.data;

import java.util.*;
import java.util.function.Function;

public class DataTable extends BaseTable {

    /**
     * Here we store table data column-wise, so that we won't
     * have duplicated column names, which requires some more memories.
     * WARNING: When adding data, null is added for missing column to ensure
     *  columns have the same size.
     */
    private final Map<String, IColumn> columns = new HashMap<>();

    public DataTable(String name) {
        super(name);
    }

    @Override
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public Collection<Object> getRow(int row) {
        var values = new ArrayList<>();
        columns.forEach((name, column) -> values.add(column.getValue(row)));
        return values;
    }

    @Override
    public boolean hasColumn(String name) {
        return columns.containsKey(name);
    }

    @Override
    public boolean hasColumn(IColumn column) {
        return columns.containsValue(column);
    }

    @Override
    public IColumn getColumn(String name) {
        if (!columns.containsKey(name)) {
            throw new IllegalArgumentException("Column with name " + name + " does not exist");
        }
        return columns.get(name);
    }

    @Override
    public Collection<String> getColumnNames() {
        return columns.keySet();
    }

    @Override
    public Set<Map.Entry<String, IColumn>> getColumnEntries() {
        return columns.entrySet();
    }

    @Override
    public ITable duplicate(boolean schemaOnly) {
        var table = DataTable.builder().withName(name);
        for (var column : columns.values()) {
            table.withColumn(column.duplicate(schemaOnly));
        }
        return table.build();
    }

    @Override
    public IColumn asColumn() {
        if (columns.size() != 1) {
            throw new IllegalStateException("Table has no or more than one column");
        }
        return columns.values().iterator().next();
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
        return new DataTableBuilder();
    }

    @Override
    protected Collection<IColumn> _getColumns() {
        return columns.values();
    }

    @Override
    protected IColumn _removeColumn(String name) {
        if (!columns.containsKey(name)) {
            throw new IllegalArgumentException("Column with name " + name + " does not exist");
        }
        return columns.remove(name);
    }

    @Override
    protected IColumn _addColumn(IColumn column) {
        if (columns.containsKey(column.name())) {
            throw new IllegalArgumentException("Column with name " + column.name() + " already exists");
        }
        columns.put(column.name(), column);
        return column;
    }

    private static class DataTableBuilder implements Builder {
        private DataTable dataTable;

        @Override
        public Builder withName(String name) {
            dataTable = new DataTable(name);
            return this;
        }

        @Override
        public Builder withColumn(Function<IColumn.Builder, IColumn.Builder> builder) {
            if (dataTable == null) {
                throw new IllegalStateException("Table name not set");
            }
            dataTable.addColumn(builder);
            return this;
        }

        @Override
        public Builder withColumn(IColumn column) {
            if (dataTable == null) {
                throw new IllegalStateException("Table name not set");
            }
            dataTable.addColumn(column);
            return this;
        }

        @Override
        public ITable build() {
            if (dataTable == null) {
                throw new IllegalStateException("Table name not set");
            }
            return dataTable;
        }
    }
}
