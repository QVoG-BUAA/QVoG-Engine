package cn.edu.buaa.qvog.engine.db;

import cn.edu.buaa.qvog.engine.db.cache.ICacheProxy;
import cn.edu.buaa.qvog.engine.db.gremlin.IGremlinDb;

public interface IDbContext {
    IGremlinDb getGremlinDb();

    ICacheProxy getCacheProxy();

    void close();
}
