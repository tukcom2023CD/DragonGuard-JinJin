package com.dragonguard.backend.config.init;

import com.dragonguard.backend.blockchain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@DependsOn("blockchainConfig")
public class BlockchainDeployInit {
    private final TransactionService transactionService;

    @PostConstruct
    public void initDeploy() {
        transactionService.deploy();
    }
}
