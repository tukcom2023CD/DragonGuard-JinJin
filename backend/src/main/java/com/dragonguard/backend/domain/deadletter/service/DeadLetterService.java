package com.dragonguard.backend.domain.deadletter.service;

import com.dragonguard.backend.domain.deadletter.entity.DeadLetter;
import com.dragonguard.backend.domain.deadletter.mapper.DeadLetterMapper;
import com.dragonguard.backend.domain.deadletter.repository.DeadLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter 서비스
 */

@Service
@RequiredArgsConstructor
public class DeadLetterService {

    private final DeadLetterRepository deadLetterRepository;
    private final DeadLetterMapper deadLetterMapper;

    public void saveFailedMessage(final String topic, final String key, final int partitionId, final Long offset, final String value, final String errorMessage) {
        deadLetterRepository.save(deadLetterMapper.toEntity(topic, key, partitionId, offset, value, errorMessage));
    }

    public List<DeadLetter> findNotRetried() {
        return deadLetterRepository.findAllByIsRetriedIsFalse();
    }
}
