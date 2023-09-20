package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoMemberDetailsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoMemberFacade;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 kafka로 부터 받아와 처리하는 consumer
 */

@Component
@RequiredArgsConstructor
public class KafkaGitRepoConsumer implements KafkaConsumer<GitRepoKafkaResponse> {
    private static final Integer DEFAULT_INDEX = 0;
    private final GitRepoMemberFacade gitRepoMemberFacade;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.git-repos", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final String message) {
        final GitRepoKafkaResponse response = readValue(message);

        final List<GitRepoMemberDetailsResponse> result = response.getResult();

        if (Objects.isNull(result) || result.isEmpty()) {
            return;
        }

        final List<GitRepoMemberResponse> gitRepoMemberResponses = result.stream()
                .map(res -> new GitRepoMemberResponse(res, memberService.findMemberOrSaveWithRole(res.getMember(), Role.ROLE_USER, AuthStep.NONE)))
                .collect(Collectors.toList());

        gitRepoMemberFacade.updateOrSaveAll(gitRepoMemberResponses, result.get(DEFAULT_INDEX).getGitRepo());
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public GitRepoKafkaResponse readValue(final String message) {
        return objectMapper.readValue(message, GitRepoKafkaResponse.class);
    }
}
