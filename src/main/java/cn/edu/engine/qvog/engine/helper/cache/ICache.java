package cn.edu.engine.qvog.engine.helper.cache;

/**
 * NodeCache is used to cache the node in the graph.
 * Replace all plain gets with cache get.
 */
public interface ICache<K, V> {
    void put(K vertex, V node);

    V get(K vertex);
}
