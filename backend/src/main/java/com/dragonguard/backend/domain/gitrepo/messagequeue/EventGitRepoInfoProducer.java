package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 레포지토리 kafka 요청을 보내는 consumer 클래스
 */
@Component
@RequiredArgsConstructor
public class EventGitRepoInfoProducer implements EventProducer<GitRepoEvent> {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void send(final GitRepoEvent event) {
        eventPublisher.publishEvent(event);
    }
}
