package cn.edu.engine.qvog.engine.dsl.data;

import java.io.Serializable;

public interface IIndex<K, V> extends Serializable {
    /**
     * Put a key-value pair into the index.
     *
     * @param key   The key.
     * @param value The value.
     */
    void put(K key, V value);

    /**
     * Get the value from the index by the key.
     *
     * @param key The key.
     * @return The value.
     */
    V get(K key);

    /**
     * Remove the key-value pair from the index by the key.
     *
     * @param key The key
     */
    void remove(K key);


    /**
     * Whether the index contains the key or not.
     *
     * @param key The key.
     * @return Whether the index contains the key.
     */
    boolean contains(K key);
}
