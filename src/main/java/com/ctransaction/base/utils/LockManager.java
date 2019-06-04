package com.ctransaction.base.utils;

import com.ctransaction.base.exception.LockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LockManager {

    static Logger logger = LoggerFactory.getLogger(LockManager.class);
    //本地锁池
    private static ConcurrentHashMap<String, AtomicBoolean> localLockPool = new ConcurrentHashMap<>();
    private static final int waitMillionSec = 1000 * 2;
    private static final String lock = "LK";
    private static final String release_lock = "RK";
    private static final String set_if_not_exist = "NX";
    private static final String set_with_expire_time = "PX";
    private static final String success_result = "OK";
    private static final String release_script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static ShardedJedisPool shardedJedisPool;

    @Autowired(required = false)
    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }


    public static LockObject getLock(String key) throws Exception {
        try {
            //先获取本地锁
            if (getLocalLock(key)) {
                //加锁 排队查询
                for (int mSec = 200; mSec <= waitMillionSec; mSec += mSec) {
                    try (ShardedJedis client = shardedJedisPool.getResource()) {
                        String redisValue = client.set(key, lock, set_if_not_exist, set_with_expire_time, waitMillionSec);
                        if (success_result.equals(redisValue)) {
                            return new LockObject(key);
                        }
                        Thread.sleep(mSec);
                    }
                }
                throw new Exception("get lock failed");
            }
        } catch (LockException ex) {
        } catch (Exception ex) {
            releaseLocalLock(key);
            logger.error("get redis lock error", ex);
            throw ex;
        }
        return null;
    }

    /*
    获取本地锁
     */
    private static boolean getLocalLock(String key) throws LockException {
        //锁池
        AtomicBoolean lock = getLockByKey(key);
        for (int mSec = 200; mSec < waitMillionSec; mSec += mSec) {
            //原子锁
            if (lock.compareAndSet(false, true)) {
                return true;
            }
            try {
                Thread.sleep(mSec);
            } catch (InterruptedException e) {
                logger.error("get local lock error", e);
            }
        }
        throw new LockException("throw ");
    }

    /*
    同步方法获取锁标志
     */
    private static synchronized AtomicBoolean getLockByKey(String key) {
        if (!localLockPool.containsKey(key)) {
            localLockPool.put(key, new AtomicBoolean(false));
        }
        return localLockPool.get(key);
    }

    /*
    释放本地锁
     */
    private static void releaseLocalLock(String key) {
        AtomicBoolean lock = localLockPool.get(key);
        lock.set(false);
    }

    /*
    释放redis锁
     */
    private static void releaseRedisLock(String key, String value) {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.getShard(key).eval(release_script, Collections.singletonList(key), Collections.singletonList(value));
        } catch (Exception ex) {
            logger.error("release redis lock error", ex);
        }
    }


    /**
     * 分布锁对象
     */
    public static class LockObject implements AutoCloseable {

        private String key;

        public LockObject(String key) {
            this.key = key;
        }

        @Override
        public void close() throws Exception {
            //释放redis锁
            releaseRedisLock(key, release_lock);
            //最后释放本地锁
            releaseLocalLock(this.key);
        }
    }


}
