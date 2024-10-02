package cn.edu.engine.qvog.engine.dsl;

import cn.edu.engine.qvog.engine.db.IDbContext;

public abstract class BaseQuery implements IQueryable {
    protected IDbContext dbContext;

    public IQueryable withDatabase(IDbContext dbContext) {
        this.dbContext = dbContext;
        return this;
    }
}
