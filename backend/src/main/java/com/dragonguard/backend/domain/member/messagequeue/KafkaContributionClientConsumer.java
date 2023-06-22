package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.ContributionClientResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 기여도 조회를 위한 요청을 처리하는 Consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaContributionClientConsumer implements KafkaConsumer<ContributionClientResponse> {
    private final MemberClientService memberClientService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.contribution.client", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        ContributionClientResponse response = readValue(message);
        Member member = memberRepository.findByGithubId(response.getGithubId())
                .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberContribution(member);
        memberService.transactionAndUpdateTier(member);
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public ContributionClientResponse readValue(String message) {
        return objectMapper.readValue(message, ContributionClientResponse.class);
    }
}
