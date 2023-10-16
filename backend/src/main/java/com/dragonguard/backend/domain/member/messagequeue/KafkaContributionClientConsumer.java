package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.ContributionClientResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 기여도 조회를 위한 요청을 처리하는 Consumer
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaContributionClientConsumer implements KafkaConsumer<ContributionClientResponse> {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.contribution.client", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload final ContributionClientResponse message, final Acknowledgment acknowledgment) {
        final Member member = memberRepository.findByGithubId(message.getGithubId())
                .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberContribution(member);
        member.validateWalletAddressAndUpdateTier();
        acknowledgment.acknowledge();
    }
}
