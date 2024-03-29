package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.RepositoryClientResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 멤버의 Repository 조회를 위한 요청을 처리하는 Consumer
 */
@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientConsumer implements KafkaConsumer {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(
            topics = "gitrank.to.backend.repository.client",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload final String message, final Acknowledgment acknowledgment)
            throws JsonProcessingException {
        final Member member =
                memberRepository
                        .findByGithubId(
                                objectMapper
                                        .readValue(message, RepositoryClientResponse.class)
                                        .getGithubId())
                        .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberGitRepoAndGitOrganization(member);
        acknowledgment.acknowledge();
    }
}
