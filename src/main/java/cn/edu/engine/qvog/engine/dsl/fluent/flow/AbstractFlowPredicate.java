package cn.edu.engine.qvog.engine.dsl.fluent.flow;

import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.*;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.MatchAll;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.MatchNone;
import cn.edu.engine.qvog.engine.helper.graph.IGraphHelper;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * use default table name to reduce some repeat
 * 1. sourceTableName: source
 * 2. sinkTableName: sink
 */
public abstract class AbstractFlowPredicate implements IFlowPredicate {
    protected final IDbContext dbContext;
    protected final String sourceTableName;
    protected final String sinkTableName;
    protected final String barrierTableName;
    protected final String alias;
    protected final Set<String> affectedTableNames;
    protected final IGraphHelper helper = Environment.getInstance().getGraphHelper();
    protected Boolean addSysExit;
    protected Boolean addEntryExit;
    protected Boolean flowSensitive;
    protected IValuePredicate specialSinkPredicate;

    public AbstractFlowPredicate(IDbContext dbContext, String sourceTableName, String sinkTableName,
                                 String barrierTableName, String alias,
                                 Boolean addSysExit, Boolean addEntryExit,
                                 IValuePredicate specialSinkPredicate) {
        this(dbContext, sourceTableName, sinkTableName,
                barrierTableName, alias, true, addSysExit, addEntryExit, specialSinkPredicate);
    }

    public AbstractFlowPredicate(IDbContext dbContext, String sourceTableName, String sinkTableName,
                                 String barrierTableName, String alias,
                                 Boolean flowSensitive, Boolean addSysExit, Boolean addEntryExit,
                                 IValuePredicate specialSinkPredicate) {
        this.dbContext = dbContext == null ? Environment.getInstance().getDbContext() : dbContext;

        this.sourceTableName = sourceTableName == null ? "source" : sourceTableName;
        this.sinkTableName = sinkTableName == null ? "sink" : sinkTableName;
        this.barrierTableName = barrierTableName;
        this.alias = alias;

        affectedTableNames = Stream.of(this.sourceTableName, this.sinkTableName, this.barrierTableName)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        this.addSysExit = addSysExit != null && addSysExit;
        this.addEntryExit = addEntryExit != null && addEntryExit;
        this.specialSinkPredicate = specialSinkPredicate == null ? new MatchNone() : specialSinkPredicate;
        this.flowSensitive = flowSensitive == null || flowSensitive;
    }

    @Override
    public Collection<String> getAffectedTableNames() {
        return affectedTableNames;
    }

    @Override
    public ITable exists(ITableSet tables) {
        if (sourceTableName == null) {
            throw new IllegalArgumentException("Source table name is required.");
        }

        ITable sourceTable = tables.getTable(sourceTableName);
        if (!(sourceTable.asColumn() instanceof DataColumn)) {
            throw new IllegalArgumentException("Source table must be a data table.");
        }
        ITable sinkTable = sinkTableName == null ? null : tables.getTable(sinkTableName);
        ITable barrierTable = barrierTableName == null ? null : tables.getTable(barrierTableName);
        var builder = DataTable.builder().withName(alias).withColumn(sourceTable.asColumn().duplicate());
        if (sinkTable != null) {
            builder.withColumn(buildResultColumnFromTable(sinkTable, sinkTableName));
        } else {
            builder.withColumn(DataColumn.builder().withName(sinkTableName)
                    .withIndex(IndexTypes.ValueIndex).build());
        }
        // Barrier table is not needed for now.
        // if (barrierTable != null) {
        //     builder.withColumn(barrierTable.asColumn().duplicate());
        // }
        var result = builder.withColumn(b -> b.withName(alias)).build();
        exists(sourceTable.asColumn(),
                sinkTable == null
                        ? sinkTableName == null ? null : PredicateColumn.builder().withName(sinkTableName).withPredicate(new MatchAll()).build()
                        : sinkTable.asColumn(),
                barrierTable == null
                        ? barrierTableName == null ? null : PredicateColumn.builder().withName(barrierTableName).withPredicate(new MatchNone()).build()
                        : barrierTable.asColumn(),
                result);
        return result;
    }

    private IColumn buildResultColumnFromTable(ITable table, String columnName) {
        var column = table.getColumn(columnName);
        if (column instanceof DataColumn dataColumn) {
            return dataColumn.duplicate();
        } else if (column instanceof PredicateColumn) {
            return DataColumn.builder().withName(columnName).withIndex(IndexTypes.ValueIndex).build();
        } else {
            throw new IllegalArgumentException("Unsupported column type: " + column.getClass().getSimpleName());
        }
    }

    protected abstract void exists(IColumn source, IColumn sink, IColumn barrier, ITable result);
}
