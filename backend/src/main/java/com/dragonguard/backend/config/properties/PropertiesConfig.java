package com.dragonguard.backend.config.properties;

import com.dragonguard.backend.config.blockchain.BlockchainProperties;
import com.dragonguard.backend.config.github.GithubProperties;
import com.dragonguard.backend.config.kafka.KafkaConsumerProperties;
import com.dragonguard.backend.config.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 김승진
 * @description properties 클래스들을 스프링 빈으로 등록하는 설정 클래스
 */

@Configuration
@EnableConfigurationProperties({
        GithubProperties.class,
        BlockchainProperties.class,
        RedisProperties.class,
        KafkaConsumerProperties.class})
public class PropertiesConfig {}
