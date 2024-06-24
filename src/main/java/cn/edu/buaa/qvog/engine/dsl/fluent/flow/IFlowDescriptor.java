package cn.edu.buaa.qvog.engine.dsl.fluent.flow;

import cn.edu.buaa.qvog.engine.db.IDbContext;

public interface IFlowDescriptor extends ICanSetFlowEntry {
    ICanSetFlowEntry withDatabase(IDbContext dbContext);
}
