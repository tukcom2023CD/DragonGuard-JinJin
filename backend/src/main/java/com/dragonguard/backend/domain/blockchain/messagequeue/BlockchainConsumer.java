package com.dragonguard.backend.domain.blockchain.messagequeue;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaResponse;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.dragonguard.backend.global.template.service.EntityLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author 김승진
 * @description 블록체인 토큰 생성 요청을 카프카로부터 받아오는 consumer
 */

@Component
@RequiredArgsConstructor
public class BlockchainConsumer implements KafkaConsumer<BlockchainKafkaResponse> {
    private final EntityLoader<Member, UUID> memberService;
    private final BlockchainService blockchainService;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.blockchain", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload final BlockchainKafkaResponse message, final Acknowledgment acknowledgment) {
        final Member member = memberService.loadEntity(message.getMemberId());

        blockchainService.setTransaction(member, message.getAmount(), message.getContributeType());
        acknowledgment.acknowledge();
    }
}
