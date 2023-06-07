package com.dragonguard.backend.config.init;

import com.dragonguard.backend.domain.blockchain.service.SmartContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 김승진
 * @description 블록체인 스마트 컨트랙트 배포를 위한 클래스
 */

@Component
@RequiredArgsConstructor
@DependsOn("blockchainConfig")
public class BlockchainDeploy {
    private final SmartContractService smartContractService;

    @PostConstruct
    public void initDeploy() {
        smartContractService.deploy();
    }
}
