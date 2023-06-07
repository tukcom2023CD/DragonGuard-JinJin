package com.dragonguard.backend.config.blockchain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 블록체인 관련 application.yml의 환경변수를 받아오는 클래스
 */

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    private String byteCode;
    private String password;
    private String abi;
    private String keyring;
}
