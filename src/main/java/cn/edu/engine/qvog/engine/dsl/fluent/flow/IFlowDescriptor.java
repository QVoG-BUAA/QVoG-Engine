package cn.edu.engine.qvog.engine.dsl.fluent.flow;

import cn.edu.engine.qvog.engine.db.IDbContext;

public interface IFlowDescriptor extends ICanSetFlowEntry {
    ICanSetFlowEntry withDatabase(IDbContext dbContext);
}
