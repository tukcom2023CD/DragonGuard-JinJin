package com.dragonguard.backend.config.blockchain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "blockchain")
@Component
public class BlockchainProperties {
    private String privateKey;
    private String byteCode;
    private String password;
    private String walletKey;
    private String contractAddress;
}
