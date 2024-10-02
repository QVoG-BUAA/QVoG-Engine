package cn.edu.engine.qvog.engine.db.cache;

import java.io.Serializable;

public class NoCacheProxy implements ICacheProxy {
    @Override
    public void put(String key, Serializable value) {
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public void remove(String key) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void close() {
    }
}
