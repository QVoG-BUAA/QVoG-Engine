package cn.edu.engine.qvog.engine.dsl.data;

import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Represents a column in a table.
 * Supports adding values to the column, and getting values from the column.
 * TODO: Add support for removing values from the column.
 */
public interface IColumn extends Serializable {
    /**
     * clear the value
     */
    void clear();

    /**
     * Get the name of the column.
     *
     * @return The name of the column.
     */
    String name();

    /**
     * Get the size of the column.
     *
     * @return The size of the column.
     */
    int count();

    /**
     * Add a collection of values to the column.
     *
     * @param values The values to add.
     */
    default void addValues(Collection<Object> values) {
        for (var value : values) {
            addValue(value);
        }
    }

    /**
     * Add a row to the column.
     *
     * @param value The value of the row.
     */
    void addValue(Object value);

    /**
     * Get the value of the column at the index.
     *
     * @param index The index of the value.
     * @return The value of the column at the index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    Object getValue(int index);

    /**
     * Get the value of the column at the index.
     *
     * @param key The key of the value.
     * @return The value of the column at the index. Returns null if the key
     * does not exist.
     */
    Object getValue(Object key);

    /**
     * Whether the column is indexed. If indexed, then the column has a
     * key set, and the key set is used to speed up the search.
     *
     * @return Whether the column is indexed.
     */
    boolean hasIndex();

    default boolean containsKey(Object key) {
        return containsKey(k -> k.equals(key));
    }

    /**
     * Whether the column contains the key.
     * This should only be called if the column is indexed.
     *
     * @param predicate The predicate to test the key.
     * @return Whether the column contains the key.
     */
    boolean containsKey(Predicate<Object> predicate);

    /**
     * Whether the column contains the value.
     * This will perform a linear search no matter whether the column is
     * indexed or not.
     *
     * @param predicate The predicate to test the value.
     * @return Whether the column contains the value.
     */
    boolean containsValue(Predicate<Object> predicate);

    /**
     * Whether the column contains the value.
     * By default, this method uses the equals method to test the value.
     * And if not specified, will check the address of the value.
     *
     * @param value The value to test.
     * @return Whether the column contains the value.
     */
    boolean containsValue(Object value);

    /**
     * Duplicate the column only with the schema.
     *
     * @return The duplicated column.
     */
    default IColumn duplicate() {
        return duplicate(true);
    }

    /**
     * Duplicate the column. If schemaOnly is true, then only the schema
     * of the column is duplicated, and the values are not duplicated.
     *
     * @param schemaOnly Whether to duplicate the schema only.
     * @return The duplicated column.
     */
    IColumn duplicate(boolean schemaOnly);

    /**
     * Get the iterator of the column.
     *
     * @return The iterator of the column.
     */
    Iterator<Object> iterator();

    /**
     * This function is used to amend the column fetched from cache.
     *
     * @param newName New name for the column.
     */
    void amend(String newName);

    interface Builder {
        Builder withName(String name);

        default Builder withIndex(IndexTypes indexType) {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support withIndex");
        }

        default Builder withValues(Collection<Object> values) {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support withValues");
        }

        default Builder withPredicate(IValuePredicate predicate) {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support withPredicate");
        }

        IColumn build();
    }
}
