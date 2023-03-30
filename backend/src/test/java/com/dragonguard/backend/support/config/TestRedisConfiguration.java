package com.dragonguard.backend.support.config;

import com.dragonguard.backend.config.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.redis.connection.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {

    private final RedisServer redisServer;

    public TestRedisConfiguration(RedisProperties redisProperties) {
        this.redisServer = new RedisServer(redisProperties.getHost(), redisProperties.getPort());
    }
}
