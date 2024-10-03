package com.dragonguard.backend.domain.email.messagequeue;

import com.dragonguard.backend.domain.email.dto.kafka.EmailEvent;
import com.dragonguard.backend.domain.email.exception.EmailException;
import com.dragonguard.backend.global.template.kafka.EventConsumer;
import com.dragonguard.backend.utils.EmailSender;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.mail.MessagingException;

/**
 * @author 김승진
 * @description Kafka로 이메일을 보내기 위한 요청을 처리하는 Consumer
 */
@Component
@RequiredArgsConstructor
public class EventEmailConsumer implements EventConsumer<EmailEvent> {
    private final EmailSender emailSender;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    @Override
    public void consume(final EmailEvent event) {
        try {
            emailSender.send(event.getMemberEmail(), event.getRandom());
        } catch (final MessagingException e) {
            throw new EmailException();
        }
    }
}
