package cn.edu.buaa.qvog.engine.dsl.fluent.query;

import cn.edu.buaa.qvog.engine.db.IDbContext;

public interface IQueryDescriptor extends InitialQuery {
    InitialQuery withDatabase(IDbContext dbContext);

    IQueryDescriptor useEntryExitVirtualNode(Boolean useEntry, Boolean useExit);
}
