package cn.edu.engine.qvog.engine.dsl.fluent.query;

import cn.edu.engine.qvog.engine.db.IDbContext;

public interface IQueryDescriptor extends InitialQuery {
    InitialQuery withDatabase(IDbContext dbContext);

    IQueryDescriptor useEntryExitVirtualNode(Boolean useEntry, Boolean useExit);
}
