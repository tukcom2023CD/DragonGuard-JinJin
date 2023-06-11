package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.GithubIdKafkaResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaContributionClientConsumer {
    private final MemberClientService memberClientService;
    private final MemberService memberService;
    private final MemberQueryRepository memberQueryRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @SneakyThrows(JsonProcessingException.class)
    @KafkaListener(topics = "gitrank.to.backend.contribution.client", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        GithubIdKafkaResponse response = objectMapper.readValue(message, GithubIdKafkaResponse.class);
        Member member = memberQueryRepository.findByGithubId(response.getGithubId())
                .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberContribution(member);
        memberService.transactionAndUpdateTier(member);
    }
}
