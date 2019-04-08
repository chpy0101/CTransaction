package com.ctransaction.node;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collections;

/**
 * Created by chpy on 19/4/8.
 */
@Configuration
public class Config {

    @Value("${redis.pool.maxActive}")
    private int maxActive;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.url}")
    private String url;

    @Bean
    public JedisPoolConfig initConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxActive);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return config;
    }

    @Bean
    public ShardedJedisPool createPool(JedisPoolConfig config) {
        JedisShardInfo info = new JedisShardInfo(url);
        ShardedJedisPool pool = new ShardedJedisPool(config, Collections.singletonList(info));
        return pool;
    }
}
