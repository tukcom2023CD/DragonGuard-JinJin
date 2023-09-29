package com.dragonguard.backend.domain.email.messagequeue;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.domain.email.exception.EmailException;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.dragonguard.backend.utils.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * @author 김승진
 * @description Kafka로 이메일을 보내기 위한 요청을 처리하는 Consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaEmailConsumer implements KafkaConsumer<KafkaEmail> {
    private final EmailSender emailSender;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.email", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final String message) {
        final KafkaEmail kafkaEmail = readValue(message);
        sendEmail(kafkaEmail);
    }

    private void sendEmail(final KafkaEmail kafkaEmail) {
        try {
            emailSender.send(kafkaEmail.getMemberEmail(), kafkaEmail.getRandom());
        } catch (final MessagingException e) {
            throw new EmailException();
        }
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public KafkaEmail readValue(final String message) {
        return objectMapper.readValue(message, KafkaEmail.class);
    }
}
