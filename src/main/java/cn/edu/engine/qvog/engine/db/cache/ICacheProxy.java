package cn.edu.engine.qvog.engine.db.cache;

import java.io.Serializable;

public interface ICacheProxy {
    void put(String key, Serializable value);

    Object get(String key);

    void remove(String key);

    void clear();

    void close();
}
