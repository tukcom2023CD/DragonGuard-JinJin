package com.dragonguard.backend.domain.gitrepo.messagequeue;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineEvent;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.global.template.kafka.EventConsumer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 kafka 요청을 보내는 consumer 클래스
 */
@Component
@RequiredArgsConstructor
public class EventSparkLineConsumer implements EventConsumer<SparkLineEvent> {
    private final GitRepoService gitRepoService;

    @Async
    @Transactional
    @EventListener
    @Override
    public void consume(final SparkLineEvent sparkLineEvent) {
        gitRepoService.updateSparkLine(sparkLineEvent.getId(), sparkLineEvent.getGithubToken());
    }
}
