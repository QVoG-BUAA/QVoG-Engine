package cn.edu.engine.qvog.engine.dsl.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a set of tables used in query.
 */
public interface ITableSet extends Serializable {
    /**
     * Add an existing table to the set.
     *
     * @param table The table to add.
     */
    void addTable(ITable table);

    /**
     * Add an empty table to the set.
     *
     * @param name Name of the table.
     */
    void addTable(String name);

    /**
     * Add a table to the set via an explicit builder.
     *
     * @param builder The builder of the table.
     */
    void addTable(Function<ITable.Builder, ITable.Builder> builder);

    /**
     * Check if the set has a table by name.
     *
     * @param name Name of the table.
     * @return True if the set has the table, false otherwise.
     */
    boolean hasTable(String name);

    /**
     * Get a table by name.
     *
     * @param name Name of the table.
     * @return The table.
     */
    ITable getTable(String name);

    /**
     * Remove a table by name.
     *
     * @param name Name of the table.
     * @return The removed table.
     */
    ITable removeTable(String name);

    /**
     * Remove a table by object. This table should already be in the set.
     *
     * @param table The table to remove.
     * @return The removed table.
     * @throws IllegalArgumentException If the table is not in the set.
     */
    ITable removeTable(ITable table);

    /**
     * Get the number of tables in the set.
     *
     * @return The number of tables.
     */
    int count();

    /**
     * Get the names of the tables in the set.
     *
     * @return The names of the tables.
     */
    Set<String> getTableNames();

    /**
     * Get the tables in the set.
     *
     * @return The tables.
     */
    Collection<ITable> getTables();

    /**
     * Get the names and tables in the set.
     *
     * @return The names and tables.
     */
    Set<Map.Entry<String, ITable>> getTableEntries();

    /**
     * Get the tables as a single table. Only works if there is only one table in the set.
     *
     * @return The table.
     * @throws IllegalArgumentException If there is not exactly one table in the set.
     */
    ITable asTable();
}
