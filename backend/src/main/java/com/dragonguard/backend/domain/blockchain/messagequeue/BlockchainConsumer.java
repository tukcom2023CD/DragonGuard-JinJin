package com.dragonguard.backend.domain.blockchain.messagequeue;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaResponse;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
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
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.blockchain", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final String message) {
        final BlockchainKafkaResponse response = readValue(message);

        final Member member = memberService.loadEntity(response.getMemberId());

        blockchainService.setTransaction(member, response.getAmount(), response.getContributeType());
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public BlockchainKafkaResponse readValue(final String message) {
        return objectMapper.readValue(message, BlockchainKafkaResponse.class);
    }
}
