package com.dragonguard.backend.domain.blockchain.messagequeue;

import com.dragonguard.backend.domain.blockchain.dto.kafka.SmartContractKafkaResponse;
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
 * @description 블록체인 스마트 컨트랙트 요청을 보내는 consumer
 */

@Component
@RequiredArgsConstructor
public class SmartContractConsumer implements KafkaConsumer<SmartContractKafkaResponse> {
    private final EntityLoader<Member, UUID> memberService;
    private final BlockchainService blockchainService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.smartcontract", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        SmartContractKafkaResponse response = readValue(message);

        Member member = memberService.loadEntity(response.getMemberId());

        blockchainService.sendSmartContract(
                member,
                response.getAmount(),
                blockchainService.loadEntity(response.getBlockchainId()));
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public SmartContractKafkaResponse readValue(String message) {
        return objectMapper.readValue(message, SmartContractKafkaResponse.class);
    }
}
