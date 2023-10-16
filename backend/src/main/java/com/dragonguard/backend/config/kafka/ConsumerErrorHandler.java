package com.dragonguard.backend.config.kafka;

import com.dragonguard.backend.domain.deadletter.service.DeadLetterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter Queue를 통한 재시도 작업 담당 클래스
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerErrorHandler {

    private final DeadLetterService deadLetterService;
    private final ObjectMapper objectMapper;

    public void postProcessDltMessage(final ConsumerRecord<String, String> record,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic,
                                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) final int partitionId,
                                        @Header(KafkaHeaders.OFFSET) final Long offset,
                                        @Header(KafkaHeaders.GROUP_ID) final String groupId) throws JsonProcessingException, MessagingException {
        final String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(record.value());
        final String key = record.key();
        log.info("[DLT] received message={} with key={} partitionId={}, offset={}, topic={}, groupId={}", value, key, partitionId, offset, topic, groupId);

        deadLetterService.saveFailedMessage(topic, key, partitionId, offset, value, groupId);
    }
}
