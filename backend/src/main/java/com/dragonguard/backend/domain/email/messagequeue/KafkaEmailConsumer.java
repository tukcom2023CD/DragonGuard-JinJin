package com.dragonguard.backend.domain.email.messagequeue;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 이메일을 보내기 위한 요청을 처리하는 Consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaEmailConsumer implements KafkaConsumer<KafkaEmail> {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.email", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message, Acknowledgment acknowledgment) {
        KafkaEmail kafkaEmail = readValue(message);
        emailService.sendEmail(kafkaEmail.getMemberEmail(), kafkaEmail.getRandom());
        acknowledgment.acknowledge();
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public KafkaEmail readValue(String message) {
        return objectMapper.readValue(message, KafkaEmail.class);
    }
}
