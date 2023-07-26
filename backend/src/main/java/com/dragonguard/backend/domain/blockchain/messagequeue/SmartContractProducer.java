package com.dragonguard.backend.domain.blockchain.messagequeue;

import com.dragonguard.backend.domain.blockchain.dto.kafka.SmartContractKafkaRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 블록체인 스마트 컨트랙트 요청을 카프카로 보내는 producer
 */

@Component
@RequiredArgsConstructor
public class SmartContractProducer implements KafkaProducer<SmartContractKafkaRequest> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(SmartContractKafkaRequest request) {
        kafkaTemplate.send("gitrank.to.backend.smartcontract", "blockchain", request);
    }
}
