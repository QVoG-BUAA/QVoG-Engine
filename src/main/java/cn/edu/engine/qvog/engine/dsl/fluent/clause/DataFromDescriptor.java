package cn.edu.engine.qvog.engine.dsl.fluent.clause;

import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.data.ITable;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;
import cn.edu.engine.qvog.engine.helper.graph.GraphFilter;

public class DataFromDescriptor extends BaseFromDescriptor {
    private final IValuePredicate predicate;
    private final String cacheTag;
    private ITable table = null;

    DataFromDescriptor(String alias, Class<?> clazz, boolean strict, String cacheTag) {
        super(alias);
        this.predicate = value -> strict ? value.getClass().equals(clazz) : clazz.isAssignableFrom(value.getClass());
        this.cacheTag = cacheTag == null ? "from:" + clazz.getSimpleName() + ":" + strict : cacheTag;
    }

    DataFromDescriptor(String alias, IValuePredicate predicate, String cacheTag) {
        super(alias);
        this.predicate = predicate;
        this.cacheTag = cacheTag;
    }

    @Override
    public String getCacheTag() {
        return cacheTag;
    }

    @Override
    public ITable fetchData(IDbContext dbContext) {
        if (table != null) {
            return table;
        }

        if (cacheTag != null) {
            var cache = dbContext.getCacheProxy();
            var cached = cache.get(cacheTag);
            if (cached != null) {
                table = (ITable) cached;
                table.amend(alias);
                return table;
            }
        }

        var gremlinDb = dbContext.getGremlinDb();
        table = GraphFilter.open().withDb(gremlinDb).withPredicate(predicate).filter(alias);
        if (cacheTag != null) {
            dbContext.getCacheProxy().put(cacheTag, table);
        }

        return table;
    }
}
