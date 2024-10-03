package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.GitRepoEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventGitRepoProducerImpl implements EventProducer<GitRepoEvent> {
    @Override
    public void send(GitRepoEvent request) {}
}
