package com.dragonguard.backend.domain.deadletter.service;

import com.dragonguard.backend.domain.deadletter.entity.DeadLetter;
import com.dragonguard.backend.domain.deadletter.mapper.DeadLetterMapper;
import com.dragonguard.backend.domain.deadletter.repository.DeadLetterRepository;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.utils.EmailSender;
import lombok.RequiredArgsConstructor;

import javax.mail.MessagingException;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter 서비스
 */

@TransactionService
@RequiredArgsConstructor
public class DeadLetterService {

    private final DeadLetterRepository deadLetterRepository;
    private final DeadLetterMapper deadLetterMapper;
    private final EmailSender emailSender;

    public void saveFailedMessage(final String topic, final String key, final int partitionId, final Long offset, final String value, final String groupId) throws MessagingException {
        final DeadLetter deadLetter = deadLetterRepository.save(deadLetterMapper.toEntity(topic, key, partitionId, offset, value, groupId));
        sendToAdminEmail(deadLetter);
    }

    private void sendToAdminEmail(final DeadLetter deadLetter) throws MessagingException {
        emailSender.sendDeadLetter(deadLetter);
    }
}
