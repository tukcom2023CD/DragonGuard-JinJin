package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.RepositoryEvent;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.EventConsumer;

import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 멤버의 Repository 조회를 위한 요청을 처리하는 Consumer
 */
@Component
@RequiredArgsConstructor
public class EventRepositoryClientConsumer implements EventConsumer<RepositoryEvent> {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;

    @Async
    @Transactional
    @EventListener
    @Override
    public void consume(final RepositoryEvent event) {
        final Member member =
                memberRepository
                        .findByGithubId(event.getGithubId())
                        .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberGitRepoAndGitOrganization(member);
    }
}
