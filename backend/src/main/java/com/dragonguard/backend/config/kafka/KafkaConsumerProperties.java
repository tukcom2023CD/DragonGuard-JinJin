package com.dragonguard.backend.config.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author 김승진
 * @description Kafka에 쓰일 환경변수들을 가져오는 클래스
 */

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperties {
    private final String groupId;
    private final boolean autoCommit;
    private final String autoOffsetReset;
}
