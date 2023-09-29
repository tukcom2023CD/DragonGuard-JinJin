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
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 멤버의 Repository 조회를 위한 요청을 처리하는 Consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaRepositoryClientConsumer implements KafkaConsumer<RepositoryClientResponse> {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.repository.client", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final String message) {
        final RepositoryClientResponse response = readValue(message);
        final Member member = memberRepository.findByGithubId(response.getGithubId())
                .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberGitRepoAndGitOrganization(member);
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public RepositoryClientResponse readValue(final String message) {
        return objectMapper.readValue(message, RepositoryClientResponse.class);
    }
}
