package cn.edu.engine.qvog.engine.dsl.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a set of tables.
 */
public class TableSet implements ITableSet {
    private final Map<String, ITable> tables = new HashMap<>();

    @Override
    public void addTable(ITable table) {
        if (tables.containsKey(table.name())) {
            throw new IllegalArgumentException("Table already exists: " + table.name());
        }
        tables.put(table.name(), table);
    }

    @Override
    public void addTable(String name) {
        addTable(builder -> builder.withName(name));
    }

    @Override
    public void addTable(Function<ITable.Builder, ITable.Builder> builder) {
        var table = builder.apply(DataTable.builder()).build();
        addTable(table);
    }

    @Override
    public boolean hasTable(String name) {
        return tables.containsKey(name);
    }

    @Override
    public ITable getTable(String name) {
        if (!hasTable(name)) {
            throw new IllegalArgumentException("Table not found: " + name);
        }
        return tables.get(name);
    }

    @Override
    public ITable removeTable(String name) {
        if (!hasTable(name)) {
            throw new IllegalArgumentException("Table not found: " + name);
        }
        return tables.remove(name);
    }

    @Override
    public ITable removeTable(ITable table) {
        if (!tables.containsValue(table)) {
            throw new IllegalArgumentException("Table not found: " + table.name());
        }
        return tables.remove(table.name());
    }

    @Override
    public int count() {
        return tables.size();
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public Collection<ITable> getTables() {
        return tables.values();
    }

    @Override
    public Set<Map.Entry<String, ITable>> getTableEntries() {
        return tables.entrySet();
    }

    @Override
    public ITable asTable() {
//        if (tables.size() != 1) {
//            throw new IllegalArgumentException("Table set does not contain exactly one table");
//        }
        return tables.values().iterator().next();
    }
}
