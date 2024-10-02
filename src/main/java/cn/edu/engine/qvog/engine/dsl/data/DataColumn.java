package cn.edu.engine.qvog.engine.dsl.data;

import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a column in a table.
 * Supports adding values to the column, and getting values from the column.
 * TODO: Add support for removing values from the column.
 */
public class DataColumn extends BaseColumn {
    private final ArrayList<Object> values;
    private final IIndex<Object, Integer> index;
    private final IndexTypes indexType;
    /**
     * WARNING: keyFunction is not serializable! It will be null after deserialization.
     */
    private transient Function<Object, Object> keyFunction;
    private int size;

    /**
     * Create a column with the name and the key function. This way, the column
     * is indexed.
     *
     * @param name        The name of the column.
     * @param keyFunction The key function of the column.
     */
    private DataColumn(String name, IndexTypes indexType) {
        this.indexType = indexType;
        this.name = name;
        this.size = 0;
        this.values = new ArrayList<>();
        if (indexType == IndexTypes.NoIndex) {
            index = null;
            keyFunction = null;
        } else {
            index = new MapIndex<>();
            keyFunction = IndexProvider.getIndexFunction(indexType);
        }
    }

    @Override
    public void clear() {
        this.size = 0;
        this.values.clear();
    }

    @Override
    public int count() {
        return this.size;
    }

    @Override
    public void addValues(Collection<Object> values) {
        if (hasIndex()) {
            this.values.ensureCapacity(size + values.size());
            for (var value : values) {
                addValue(value);
            }
        } else {
            this.values.addAll(values);
            size += values.size();
        }
    }

    @Override
    public void addValue(Object value) {
        if (hasIndex() && value != null) {
            index.put(getKey(value), size);
        }
        values.add(value);
        size++;
    }

    @Override
    public Object getValue(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds " + size);
        }
        return values.get(index);
    }

    @Override
    public Object getValue(Object key) {
        if (!hasIndex()) {
            throw new UnsupportedOperationException("Column is not indexed");
        }
        Integer entry = index.get(key);
        return entry == null ? null : values.get(entry);
    }

    @Override
    public boolean hasIndex() {
        return indexType != IndexTypes.NoIndex;
    }

    @Override
    public boolean containsKey(Predicate<Object> predicate) {
        if (!hasIndex()) {
            throw new UnsupportedOperationException("Column is not indexed");
        }
        return index.contains(predicate);
    }

    @Override
    public boolean containsValue(Predicate<Object> predicate) {
        return values.stream().anyMatch(predicate);
    }

    @Override
    public boolean containsValue(Object value) {
        Value value1 = (Value) value;
        for (Object object : values) {
            Value value2 = (Value) object;
            if (value1.getNode().id() == value2.getNode().id()) {
                return true;
            }
        }

        if (hasIndex()) {
            return index.contains(getKey(value));
        }
        return values.contains(value);
    }

    @Override
    public IColumn duplicate(boolean schemaOnly) {
        var column = DataColumn.builder().withName(name).withIndex(indexType).build();
        if (!schemaOnly) {
            column.addValues(values);
        }
        return column;
    }

    @Override
    public Iterator<Object> iterator() {
        return values.iterator();
    }

    @Override
    public void amend(String newName) {
        name = newName;
        if (hasIndex()) {
            keyFunction = IndexProvider.getIndexFunction(indexType);
        }
    }

    public static Builder builder() {
        return new DataColumnBuilder();
    }

    private Object getKey(Object value) {
        return keyFunction.apply(value);
    }

    private static class DataColumnBuilder implements IColumn.Builder {
        private String name;
        private IndexTypes indexType = IndexTypes.NoIndex;
        private Collection<Object> values;

        @Override
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder withIndex(IndexTypes indexType) {
            this.indexType = indexType;
            return this;
        }

        @Override
        public Builder withValues(Collection<Object> values) {
            this.values = values;
            return this;
        }

        @Override
        public IColumn build() {
            if (name == null) {
                throw new IllegalArgumentException("Name is required for column");
            }
            var column = new DataColumn(name, indexType);
            if (values != null) {
                column.addValues(values);
            }
            return column;
        }
    }
}
