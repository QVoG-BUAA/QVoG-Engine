package cn.edu.engine.qvog.engine.dsl;

import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.fluent.query.CompleteQuery;

public interface IQueryable {
    IQueryable withDatabase(IDbContext dbContext);

    default String getQueryName() {
        return this.getClass().getSimpleName();
    }

    CompleteQuery run();
}
