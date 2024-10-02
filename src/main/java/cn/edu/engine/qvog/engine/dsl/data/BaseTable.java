package cn.edu.engine.qvog.engine.dsl.data;

import java.util.*;
import java.util.function.Function;

public abstract class BaseTable implements ITable {
    protected String name;
    protected int size;

    public BaseTable(String name) {
        this.name = name;
        this.size = 0;
    }

    @Override
    public void clear() {
        this.size = 0;
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public int count() {
        return size;
    }

    @Override
    public void addColumn(Function<IColumn.Builder, IColumn.Builder> builder, Object defaultValue) {
        IColumn column = builder.apply(DataColumn.builder()).build();
        if (hasColumn(column.name())) {
            throw new IllegalArgumentException("Column with name " + column.name() + " already exists");
        }

        if (size > 0) {
            column.addValues(Collections.nCopies(size, defaultValue));
        }
        _addColumn(column);
    }

    @Override
    public void addColumn(IColumn column) {
        if (isEmpty()) {
            _addColumn(column);
            size = column.count();
            return;
        }

        if (hasColumn(column.name())) {
            throw new IllegalArgumentException("Column with name " + column.name() + " already exists");
        }

        // Check if the column has the same size as the table.
        int newSize = Math.max(size, column.count());
        if (column.count() < newSize) {
            column.addValues(Collections.nCopies(newSize - column.count(), null));
        }
        if (size < newSize) {
            for (var col : _getColumns()) {
                col.addValues(Collections.nCopies(newSize - size, null));
            }
        }
        size = newSize;
        // Add the column to the table.
        _addColumn(column);
    }

    @Override
    public IColumn removeColumn(String name) {
        if (!hasColumn(name)) {
            throw new IllegalArgumentException("Column with name " + name + " does not exist");
        }
        return _removeColumn(name);
    }

    @Override
    public IColumn removeColumn(IColumn column) {
        if (!hasColumn(column)) {
            throw new IllegalArgumentException("Column with name " + column.name() + " does not exist");
        }
        return _removeColumn(column.name());
    }

    @Override
    public ITable addRow(Map<String, Object> row) {
        for (var column : row.entrySet()) {
            if (hasColumn(column.getKey())) {
                getColumn(column.getKey()).addValue(column.getValue());
            }
        }
        // Compensate for missing columns.
        for (var column : _getColumns()) {
            if (column.count() == size) {
                column.addValue(null);
            }
        }
        size++;
        return this;
    }

    @Override
    public ITable addRow(Object value) {
        if (_getColumns().size() != 1) {
            throw new IllegalArgumentException("Table has more than one column");
        }
        addRow(Map.of(_getColumns().iterator().next().name(), value));
        return this;
    }

    @Override
    public Map<String, Object> getRowWithHeader(int row) {
        if (row < 0 || row >= size) {
            throw new IndexOutOfBoundsException("Row index " + row + " out of bounds " + size);
        }
        var result = new HashMap<String, Object>();
        for (var column : _getColumns()) {
            result.put(column.name(), column.getValue(row));
        }
        return result;
    }

    @Override
    public Iterator<Map<String, Object>> iteratorWithHeader() {
        return new Iterator<>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public Map<String, Object> next() {
                return getRowWithHeader(current++);
            }
        };
    }

    @Override
    public Iterator<Collection<Object>> iterator() {
        return new Iterator<>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public Collection<Object> next() {
                return getRow(current++);
            }
        };
    }

    protected abstract IColumn _removeColumn(String name);

    protected abstract Collection<IColumn> _getColumns();

    protected abstract IColumn _addColumn(IColumn column);
}
