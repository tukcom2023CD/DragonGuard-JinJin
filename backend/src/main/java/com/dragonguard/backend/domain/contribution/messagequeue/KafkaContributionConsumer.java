package com.dragonguard.backend.domain.contribution.messagequeue;

import com.dragonguard.backend.domain.contribution.dto.kafka.ContributionKafkaResponse;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 커밋 정보를 카프카로부터 받아오는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaContributionConsumer {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @SneakyThrows(JsonProcessingException.class)
    @KafkaListener(topics = "gitrank.to.backend.contribution", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        memberService.addMemberCommitAndUpdate(objectMapper.readValue(message, ContributionKafkaResponse.class));
    }
}
