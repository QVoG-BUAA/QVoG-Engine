package cn.edu.buaa.qvog.engine.dsl;

import cn.edu.buaa.qvog.engine.db.IDbContext;

import java.io.PrintStream;

public interface IQueryable {
    IQueryable withDatabase(IDbContext dbContext);

    default String getQueryName() {
        return this.getClass().getSimpleName();
    }

    void run(String style, PrintStream output);
}
