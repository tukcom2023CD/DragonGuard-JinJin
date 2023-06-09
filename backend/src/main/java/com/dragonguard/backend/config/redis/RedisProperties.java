package com.dragonguard.backend.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author 김승진
 * @description Redis 관련 application.yml의 환경변수를 받아오는 클래스
 */

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private final String host;
    private final int port;
}
