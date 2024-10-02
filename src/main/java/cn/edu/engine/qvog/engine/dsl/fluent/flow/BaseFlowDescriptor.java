package cn.edu.engine.qvog.engine.dsl.fluent.flow;

import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

public abstract class BaseFlowDescriptor implements
        IFlowDescriptor, ICanSetFlowEntry, ICanSetFlowAlias, ICanBuildFlowPredicate {
    protected Boolean addSysExit;
    protected Boolean addEntryExit;
    protected Boolean flowSensitive;

    protected IValuePredicate specialSinkPredicate;

    protected String sourceTableName;
    protected String sinkTableName;
    protected String barrierTableName;
    protected String alias;

    // By default, the dbContext is set to the one in the Environment.
    protected IDbContext dbContext = Environment.getInstance().getDbContext();

    protected BaseFlowDescriptor() {}

    @Override
    public ICanSetFlowEntry source(String tableName) {
        this.sourceTableName = tableName;
        return this;
    }

    @Override
    public ICanSetFlowEntry sink(String tableName) {
        this.sinkTableName = tableName;
        return this;
    }

    @Override
    public ICanSetFlowEntry barrier(String tableName) {
        this.barrierTableName = tableName;
        return this;
    }

    @Override
    public ICanSetFlowEntry addSysExitAsSink(Boolean addSysExit) {
        this.addSysExit = addSysExit;
        return this;
    }

    @Override
    public ICanSetFlowEntry setFlowSensitive(Boolean flowSensitive) {
        this.flowSensitive = flowSensitive;
        return this;
    }

    @Override
    public ICanSetFlowEntry addFunctionEntryAsSink(Boolean addEntryExit) {
        this.addEntryExit = addEntryExit;
        return this;
    }

    @Override
    public ICanSetFlowEntry specialSink(IValuePredicate predicate) {
        this.specialSinkPredicate = predicate;
        return this;
    }

    @Override
    public ICanBuildFlowPredicate as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public ICanSetFlowEntry withDatabase(IDbContext dbContext) {
        this.dbContext = dbContext;
        return this;
    }
}
