package cn.edu.engine.qvog.engine.db;

import cn.edu.engine.qvog.engine.db.cache.ICacheProxy;
import cn.edu.engine.qvog.engine.db.gremlin.IGremlinDb;
import cn.edu.engine.qvog.engine.helper.LogProvider;
import com.google.inject.Inject;

public class DbContext implements IDbContext {
    private final IGremlinDb gremlinDb;
    private final ICacheProxy cacheProxy;

    @Inject
    public DbContext(IGremlinDb gremlinDb, ICacheProxy cacheProxy) {
        this.gremlinDb = gremlinDb;
        this.cacheProxy = cacheProxy;
    }

    @Override
    public IGremlinDb getGremlinDb() {
        return gremlinDb;
    }

    @Override
    public ICacheProxy getCacheProxy() {
        return cacheProxy;
    }

    @Override
    public void close() {
        try {
            gremlinDb.close();
            cacheProxy.close();
        } catch (Exception e) {
            LogProvider.DEFAULT.severe(e.getMessage());
        }
    }
}
