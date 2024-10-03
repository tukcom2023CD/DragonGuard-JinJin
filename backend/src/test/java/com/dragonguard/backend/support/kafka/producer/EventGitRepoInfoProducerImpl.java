package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoEvent;
import com.dragonguard.backend.global.template.kafka.EventProducer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EventGitRepoInfoProducerImpl implements EventProducer<GitRepoInfoEvent> {
    @Override
    public void send(GitRepoInfoEvent request) {}
}
