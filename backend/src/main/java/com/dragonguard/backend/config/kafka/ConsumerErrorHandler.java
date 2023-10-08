package com.dragonguard.backend.config.kafka;

import com.dragonguard.backend.domain.deadletter.entity.DeadLetter;
import com.dragonguard.backend.domain.deadletter.service.DeadLetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter Queue를 통한 재시도 작업 담당 클래스
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerErrorHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DeadLetterService deadLetterService;

    public void postProcessDltMessage(final ConsumerRecord<String, String> record,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic,
                                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) final int partitionId,
                                        @Header(KafkaHeaders.OFFSET) final Long offset,
                                        @Header(KafkaHeaders.EXCEPTION_MESSAGE) final String errorMessage,
                                        @Header(KafkaHeaders.GROUP_ID) final String groupId) {
        final String value = record.value();
        final String key = record.key();
        log.error("[DLT] received message={} with key={} partitionId={}, offset={}, topic={}, groupId={}", value, key, partitionId, offset, topic, groupId);
        deadLetterService.saveFailedMessage(topic, key, partitionId, offset, value, errorMessage);
    }

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 15, zone = "Asia/Seoul")
    public void retryDeadLetter() {
        final List<DeadLetter> deadLetters = deadLetterService.findNotRetried();
        deadLetters.forEach(deadLetter -> {
            kafkaTemplate.send(deadLetter.getTopic(), deadLetter.getKey(), deadLetter.getValue());
            deadLetter.delete();
        });
    }
}
