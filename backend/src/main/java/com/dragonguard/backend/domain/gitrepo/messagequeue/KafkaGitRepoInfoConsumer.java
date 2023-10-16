package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoMemberFacade;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description 레포지토리 kafka 요청을 처리하는 consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaGitRepoInfoConsumer implements KafkaConsumer<GitRepoInfoRequest> {
    private final GitRepoMemberFacade gitRepoMemberFacade;
    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.git-repos-info", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload final GitRepoInfoRequest message, final Acknowledgment acknowledgment) {
        gitRepoMemberFacade.requestToGithub(message, gitRepoMemberFacade.findEntityByName(message.getName()));
        acknowledgment.acknowledge();
    }
}
