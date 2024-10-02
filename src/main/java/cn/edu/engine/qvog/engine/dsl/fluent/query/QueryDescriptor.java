package cn.edu.engine.qvog.engine.dsl.fluent.query;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.data.ITableSet;
import cn.edu.engine.qvog.engine.dsl.data.OrderedTable;
import cn.edu.engine.qvog.engine.dsl.data.TableSet;
import cn.edu.engine.qvog.engine.dsl.fluent.clause.FromDescriptorBuilder;
import cn.edu.engine.qvog.engine.dsl.fluent.clause.ICanBuildFromDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.clause.IFromDescriptorBuilder;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.FilterPredicateDescriptor;
import cn.edu.engine.qvog.engine.dsl.fluent.filter.IFilterPredicate;
import cn.edu.engine.qvog.engine.dsl.fluent.flow.IFlowPredicate;
import cn.edu.engine.qvog.engine.dsl.lib.format.IFormatter;
import cn.edu.engine.qvog.engine.dsl.lib.format.JsonConverter;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.helper.LogProvider;
import com.sarojaba.prettytable4j.PrettyTable;
import com.sarojaba.prettytable4j.converter.GfmConverter;
import com.sarojaba.prettytable4j.converter.HtmlConverter;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class QueryDescriptor implements IQueryDescriptor, InitialQuery, SimpleQuery, FilteredQuery, CompleteQuery {
    private final Map<String, IFormatter> formatters = new HashMap<>();
    private final TableSet tables = new TableSet();
    private final Boolean useEntry = false;
    private final Boolean useExit = false;
    private ITable result;
    private IDbContext dbContext = Environment.getInstance().getDbContext();

    private QueryDescriptor() {
    }

    public static IQueryDescriptor open() {
        return new QueryDescriptor();
    }

    @Override
    public InitialQuery addFormatter(String name, IFormatter formatter) {
        formatters.put(name, formatter);
        return this;
    }

    @Override
    public InitialQuery withDatabase(IDbContext dbContext) {
        this.dbContext = dbContext;
        return this;
    }

    public IQueryDescriptor useEntryExitVirtualNode(Boolean useEntry, Boolean useExit) {
        return this;
    }

    @Override
    public SimpleQuery from(Function<IFromDescriptorBuilder, ICanBuildFromDescriptor> clause) {
        tables.addTable(clause.apply(new FromDescriptorBuilder()).build().fetchData(dbContext));
        return this;
    }

    @Override
    public CompleteQuery select(String... names) {
        var table = tables.asTable();
        var builder = OrderedTable.builder().withName("Query result");
        int i = 0;
        for (var name : names) {
            if (table.hasColumn(name)) {
                builder.withColumn(table.getColumn(name));
            } else {
                // Here, we assume that the user provides a description for the result.
                final int index = i++;
                builder.withColumn(column -> column.withName("__" + index).withValues(Collections.nCopies(table.count(), name)));
            }
        }
        result = builder.build();
        return this;
    }    @Override
    public FilteredQuery where(IFilterPredicate predicate) {
        var tableName = predicate.getTableName();
        ITable table;

        if (tableName == null) {
            table = tables.asTable();
        } else {
            table = tables.getTable(tableName);
        }

        tables.addTable(predicate.filter(tables.removeTable(table.name())));

        return this;
    }

    @Override
    public IQueryDescriptor display(String style, PrintStream output) {
        String[] columnNames = result.getColumnNames()
                .stream().map(column -> column.startsWith("__") ? "" : column)
                .toList().toArray(new String[0]);
        resultPostHandleByLanguageFeature(result, columnNames);
        PrettyTable table = PrettyTable.fieldNames(columnNames);
        var it = result.iterator();
        while (it.hasNext()) {
            table.addRow(formatRow(columnNames, it.next()));
        }
        switch (style) {
            case "console" -> {
                /* Do nothing */
            }
            case "markdown", "md" -> table.converter(new GfmConverter());
            case "html" -> table.converter(new HtmlConverter());
            case "json" -> table.converter(new JsonConverter());
            case "json-compact" -> table.converter(new JsonConverter().minified());
            default -> LogProvider.DEFAULT.warning("Unknown style: " + style + ", using default style instead.");
        }
        output.println(table);
        return this;
    }    @Override
    public FilteredQuery whereP(IValuePredicate predicate) {
        var table = tables.asTable();
        return where(FilterPredicateDescriptor.create().onTable(table.name()).where(predicate).build());
    }

    /**
     * like cxx forget to release resources
     * TODO
     */
    private void resultPostHandleByLanguageFeature(ITable result, String[] columnNames) {
    }    @Override
    public FilteredQuery where(IFlowPredicate clause) {
        ITableSet tablesToJoin = new TableSet();
        clause.getAffectedTableNames().forEach(
                tableName -> tablesToJoin.addTable(tables.removeTable(tableName))
        );
        tables.addTable(clause.exists(tablesToJoin));
        return this;
    }

    private Object[] formatRow(String[] columns, Collection<Object> row) {
        Object[] values = new Object[columns.length];
        int index = 0;
        for (var value : row) {
            values[index] = formatValue(columns[index], value);
            index++;
        }
        return values;
    }

    private String formatValue(String name, Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String stringValue) {
            return stringValue;
        }

        var formatter = formatters.get(name);
        if (formatter != null) {
            return formatter.format(value);
        }

        if (value instanceof Value valueValue) {
            return Environment.getInstance().getGraphHelper().getBriefDescription(valueValue.getNode());
        }

        return value.toString();
    }






}
