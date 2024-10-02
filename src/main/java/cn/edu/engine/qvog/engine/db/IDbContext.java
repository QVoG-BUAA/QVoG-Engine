package cn.edu.engine.qvog.engine.db;

import cn.edu.engine.qvog.engine.db.cache.ICacheProxy;
import cn.edu.engine.qvog.engine.db.gremlin.IGremlinDb;

public interface IDbContext {
    IGremlinDb getGremlinDb();

    ICacheProxy getCacheProxy();

    void close();
}
