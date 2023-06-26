package com.dragonguard.backend.domain.contribution.messagequeue;

import com.dragonguard.backend.domain.contribution.dto.kafka.ContributionKafkaResponse;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description 커밋 정보를 카프카로부터 받아오는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaContributionConsumer implements KafkaConsumer<ContributionKafkaResponse> {
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.contribution", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        memberService.addMemberContributionsAndUpdate(readValue(message));
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public ContributionKafkaResponse readValue(String message) {
        return objectMapper.readValue(message, ContributionKafkaResponse.class);
    }
}
