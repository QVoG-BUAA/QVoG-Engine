package cn.edu.engine.qvog.engine.db.cache;

import cn.edu.engine.qvog.engine.exception.CacheException;
import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.helper.Tuple;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisCacheProxy implements ICacheProxy {
    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    private final int MAGIC = 0xDEADBEEF;
    private final JedisPool pool;
    private final int shardSize;

    private RedisCacheProxy(JedisPool pool, int shardSize) {
        this.pool = pool;
        this.shardSize = shardSize;
    }

    public static RedisCacheProxy connect(JSONObject config) {
        String host = JsonHelper.getValue(config, "host");
        int port = JsonHelper.getIntValue(config, "port");
        String password = JsonHelper.tryGetValue(config, "password");
        int index = JsonHelper.tryGetIntValue(config, "db", 0);
        int shardSize = JsonHelper.tryGetIntValue(config, "shard", 0);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(64);
        var pool = new JedisPool(poolConfig, host, port, 3000, password, index);
        return new RedisCacheProxy(pool, shardSize << 10);
    }

    @Override
    public void put(String key, Serializable value) {
        putBytes(key, SerializationUtils.serialize(value));
    }

    private void putBytes(String key, byte[] value) {
        if (shardSize == 0) {
            pool.getResource().set(key.getBytes(StandardCharsets.UTF_8), value);
            return;
        }

        int shardNum = value.length / shardSize + 1;
        if (shardNum == 1) {
            pool.getResource().set(key.getBytes(StandardCharsets.UTF_8), value);
            return;
        }

        /*
         * Let's do sharding. Since this method is single-threaded,
         * we can make sure that all threads in the executor are of this task.
         */
        List<Callable<Void>> tasks = new ArrayList<>();
        byte[] mark = makeMark(value.length, shardNum);
        tasks.add(new SaveAgent(key.getBytes(StandardCharsets.UTF_8), mark));
        for (int i = 0; i < shardNum; i++) {
            int start = i * shardSize;
            int end = Math.min((i + 1) * shardSize, value.length);
            byte[] shard = new byte[end - start];
            byte[] shardKey = makeShardKey(key, i);
            System.arraycopy(value, start, shard, 0, end - start);
            tasks.add(new SaveAgent(shardKey, shard));
        }
        try {
            var futures = executor.invokeAll(tasks);
            for (var future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            throw new CacheException("Interrupted when saving sharded data.", e);
        } catch (ExecutionException e) {
            throw new CacheException("Error when saving sharded data.", e);
        }
    }

    private byte[] makeMark(int size, int shardNum) {
        byte[] mark = new byte[12];
        mark[0] = (byte) ((MAGIC >> 24) & 0xFF);
        mark[1] = (byte) ((MAGIC >> 16) & 0xFF);
        mark[2] = (byte) ((MAGIC >> 8) & 0xFF);
        mark[3] = (byte) (MAGIC & 0xFF);
        mark[4] = (byte) ((size >> 24) & 0xFF);
        mark[5] = (byte) ((size >> 16) & 0xFF);
        mark[6] = (byte) ((size >> 8) & 0xFF);
        mark[7] = (byte) (size & 0xFF);
        mark[8] = (byte) ((shardNum >> 24) & 0xFF);
        mark[9] = (byte) ((shardNum >> 16) & 0xFF);
        mark[10] = (byte) ((shardNum >> 8) & 0xFF);
        mark[11] = (byte) (shardNum & 0xFF);
        return mark;
    }

    private byte[] makeShardKey(String key, int index) {
        return (key + ":" + index).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Object get(String key) {
        long start = System.currentTimeMillis();
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return null;
        }
        long end = System.currentTimeMillis();
        System.out.println(1.0 * bytes.length / (end - start) + " bytes/ms");
        return SerializationUtils.deserialize(bytes);
    }

    private byte[] getBytes(String key) {
        byte[] result = pool.getResource().get(key.getBytes(StandardCharsets.UTF_8));
        if (result == null) {
            return null;
        }

        var mark = resolveMark(result);
        if (mark.getSecond() == 1) {
            return result;
        }
        int size = mark.getFirst();
        int shardNum = mark.getSecond();
        List<Callable<byte[]>> tasks = new ArrayList<>();
        for (int i = 0; i < shardNum; i++) {
            byte[] shardKey = makeShardKey(key, i);
            tasks.add(new LoadAgent(shardKey));
        }
        try {
            var futures = executor.invokeAll(tasks);
            byte[] value = new byte[size];
            int pos = 0;
            for (var future : futures) {
                byte[] shard = future.get();
                System.arraycopy(shard, 0, value, pos, shard.length);
                pos += shard.length;
            }
            return value;
        } catch (InterruptedException e) {
            throw new CacheException("Interrupted when saving sharded data.", e);
        } catch (ExecutionException e) {
            throw new CacheException("Error when saving sharded data.", e);
        }
    }

    private Tuple<Integer, Integer> resolveMark(byte[] mark) {
        if (mark.length != 12) {
            return Tuple.of(0, 1);
        }
        int magic = ((mark[0] & 0xFF) << 24) | ((mark[1] & 0xFF) << 16) | ((mark[2] & 0xFF) << 8) | (mark[3] & 0xFF);
        if (magic != MAGIC) {
            return Tuple.of(0, 1);
        }
        int size = ((mark[4] & 0xFF) << 24) | ((mark[5] & 0xFF) << 16) | ((mark[6] & 0xFF) << 8) | (mark[7] & 0xFF);
        int shardNum = ((mark[8] & 0xFF) << 24) | ((mark[9] & 0xFF) << 16) | ((mark[10] & 0xFF) << 8) | (mark[11] & 0xFF);
        return Tuple.of(size, shardNum);
    }

    @Override
    public void remove(String key) {
        pool.getResource().del(key);
    }

    @Override
    public void clear() {
        pool.getResource().flushDB();
    }

    @Override
    public void close() {
        pool.close();
    }

    private class SaveAgent implements Callable<Void> {
        private final byte[] key;
        private final byte[] value;

        private SaveAgent(byte[] key, byte[] value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Void call() throws Exception {
            pool.getResource().set(key, value);
            return null;
        }
    }

    private class LoadAgent implements Callable<byte[]> {
        private final byte[] key;

        public LoadAgent(byte[] key) {
            this.key = key;
        }

        @Override
        public byte[] call() {
            return pool.getResource().get(key);
        }
    }
}
