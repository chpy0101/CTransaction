package com.ctransaction.base.utils;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by chpy on 19/5/13.
 */
public class RedisLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public static boolean trtGetLock(ShardedJedis jedis,String key,int expireTime){
        return "OK".equals(
                jedis.set(key,key,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,expireTime));
    }
}
