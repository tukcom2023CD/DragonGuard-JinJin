package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoEvent;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoMemberFacade;
import com.dragonguard.backend.global.template.kafka.EventConsumer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description 레포지토리 kafka 요청을 처리하는 consumer 클래스
 */
@Component
@RequiredArgsConstructor
public class EventGitRepoInfoConsumer implements EventConsumer<GitRepoInfoEvent> {
    private final GitRepoMemberFacade gitRepoMemberFacade;

    @Async
    @Transactional
    @EventListener
    @Override
    public void consume(final GitRepoInfoEvent event) {
        gitRepoMemberFacade.requestToGithub(
                event, gitRepoMemberFacade.findEntityByName(event.getName()));
    }
}
