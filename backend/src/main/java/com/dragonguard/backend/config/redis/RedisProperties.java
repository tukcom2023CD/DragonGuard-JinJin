package com.dragonguard.backend.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.redis")
@Component
public class RedisProperties {
    private String host;
    private int port;
}
