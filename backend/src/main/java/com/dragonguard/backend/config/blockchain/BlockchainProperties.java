package com.dragonguard.backend.config.blockchain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author 김승진
 * @description 블록체인 관련 application.yml의 환경변수를 받아오는 클래스
 */

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    private final String byteCode;
    private final String password;
    private final String abi;
    private final String keyring;
    private final String contractAddress;
    private final String walletAddress;
    private final String klaytnApiUrl;
    private final String userAgent;
}
