package com.dragonguard.backend.domain.blockchain.messagequeue;

import com.dragonguard.backend.domain.blockchain.dto.kafka.BlockchainKafkaRequest;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 블록체인 토큰 생성 요청을 카프카로 보내는 Producer
 */
@Component
@RequiredArgsConstructor
public class BlockchainProducer implements KafkaProducer<BlockchainKafkaRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(final BlockchainKafkaRequest request) {
        kafkaTemplate.send("gitrank.to.backend.blockchain", "blockchain", request);
    }
}
