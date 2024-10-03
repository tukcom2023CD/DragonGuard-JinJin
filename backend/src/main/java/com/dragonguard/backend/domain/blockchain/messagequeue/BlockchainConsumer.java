package com.dragonguard.backend.domain.blockchain.messagequeue;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainEvent;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.template.kafka.EventConsumer;
import com.dragonguard.backend.global.template.service.EntityLoader;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

/**
 * @author 김승진
 * @description 블록체인 토큰 생성 요청을 카프카로부터 받아오는 consumer
 */
@Component
@RequiredArgsConstructor
public class BlockchainConsumer implements EventConsumer<BlockchainEvent> {
    private final EntityLoader<Member, UUID> memberService;
    private final BlockchainService blockchainService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    @Override
    public void consume(final BlockchainEvent event) {
        final Member member = memberService.loadEntity(event.getMemberId());
        blockchainService.setTransaction(member, event.getAmount(), event.getContributeType());
    }
}
