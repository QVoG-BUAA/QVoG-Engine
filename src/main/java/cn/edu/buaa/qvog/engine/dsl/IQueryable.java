package cn.edu.buaa.qvog.engine.dsl;

import cn.edu.buaa.qvog.engine.db.IDbContext;
import cn.edu.buaa.qvog.engine.dsl.fluent.query.CompleteQuery;

public interface IQueryable {
    IQueryable withDatabase(IDbContext dbContext);

    default String getQueryName() {
        return this.getClass().getSimpleName();
    }

    CompleteQuery run();
}
