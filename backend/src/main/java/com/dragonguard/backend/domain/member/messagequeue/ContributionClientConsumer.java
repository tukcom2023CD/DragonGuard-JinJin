package com.dragonguard.backend.domain.member.messagequeue;

import com.dragonguard.backend.domain.member.dto.kafka.ContributionEvent;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberClientService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.EventConsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description Kafka로 기여도 조회를 위한 요청을 처리하는 Consumer
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContributionClientConsumer implements EventConsumer<ContributionEvent> {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    @Override
    public void consume(final ContributionEvent event) {
        final Member member =
                memberRepository
                        .findByGithubId(event.getGithubId())
                        .orElseThrow(EntityNotFoundException::new);

        memberClientService.addMemberContribution(member);
        member.validateWalletAddressAndUpdateTier();
    }
}
