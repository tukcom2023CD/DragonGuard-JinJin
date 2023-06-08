package com.dragonguard.backend.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description Redis 관련 application.yml의 환경변수를 받아오는 클래스
 */

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private String host;
    private int port;
}
