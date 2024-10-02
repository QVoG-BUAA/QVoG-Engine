package cn.edu.engine.qvog.engine.dsl.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface ITable extends Serializable {
    /**
     * clear the size
     */
    void clear();

    /**
     * Get the name of the table.
     *
     * @return The name of the table.
     */
    String name();

    /**
     * Get the number of rows in the table.
     *
     * @return The number of rows.
     */
    int count();

    /**
     * Check if the table has any columns.
     *
     * @return True if the table has columns, false otherwise.
     */
    boolean isEmpty();

    /**
     * Add a column to the table with default value of null.
     *
     * @param builder The column action.
     */
    default void addColumn(Function<IColumn.Builder, IColumn.Builder> builder) {
        addColumn(builder, null);
    }

    /**
     * Add a column to the table.
     *
     * @param builder      The column builder.
     * @param defaultValue The default value for the column.
     */
    void addColumn(Function<IColumn.Builder, IColumn.Builder> builder, Object defaultValue);

    /**
     * Add an existing column to the table.
     *
     * @param column The column to add.
     */
    void addColumn(IColumn column);

    /**
     * Remove a column from the table.
     *
     * @param name The name of the column to remove.
     * @return The removed column.
     */
    IColumn removeColumn(String name);

    /**
     * Remove a column from the table.
     *
     * @param column The column to remove.
     * @return The removed column.
     */
    IColumn removeColumn(IColumn column);

    /**
     * Add a row to the table.
     *
     * @param row The row to add.
     * @return The table.
     */
    ITable addRow(Map<String, Object> row);

    /**
     * Add a row to the table. This is only allowed when the table has only one column.
     *
     * @param value The value of the row.
     * @return The table.
     */
    ITable addRow(Object value);

    /**
     * Get a row from the table.
     *
     * @param row The row number.
     * @return The row.
     */
    Map<String, Object> getRowWithHeader(int row);

    Collection<Object> getRow(int row);

    /**
     * Get the rows in the table.
     *
     * @return The rows as an iterator.
     */
    Iterator<Map<String, Object>> iteratorWithHeader();

    Iterator<Collection<Object>> iterator();

    /**
     * Check if the table has a column.
     *
     * @param name The name of the column.
     * @return True if the table has the column, false otherwise.
     */
    boolean hasColumn(String name);

    boolean hasColumn(IColumn column);

    /**
     * Get a column from the table.
     *
     * @param name The name of the column.
     * @return The column.
     * @throws IllegalArgumentException If the column does not exist.
     */
    IColumn getColumn(String name);

    /**
     * Get the names of the columns in the table.
     *
     * @return The names of the columns.
     */
    Collection<String> getColumnNames();

    /**
     * Get the names and columns in the table.
     *
     * @return The names and columns.
     */
    Set<Map.Entry<String, IColumn>> getColumnEntries();

    default ITable duplicate() {
        return duplicate(true);
    }

    /**
     * Duplicate the table. If schemaOnly is true, only the schema is duplicated.
     * If schemaOnly is false, the schema and data are duplicated.
     *
     * @param schemaOnly Whether to duplicate only the schema.
     * @return The duplicated table.
     */
    ITable duplicate(boolean schemaOnly);

    /**
     * Get the table as a column. This is only allowed when the table has only one column.
     *
     * @return The table as a column.
     */
    IColumn asColumn();

    /**
     * This function is used to amend the table get from cache.
     *
     * @param newName New name for the table and column.
     */
    void amend(String newName);

    /**
     * Must call {@link #withName(String)} before calling {@link #withColumn(Function)}.
     */
    interface Builder {
        Builder withName(String name);

        Builder withColumn(Function<IColumn.Builder, IColumn.Builder> builder);

        Builder withColumn(IColumn column);

        ITable build();
    }
}
