package com.dragonguard.backend.config.blockchain;

import com.dragonguard.backend.domain.blockchain.exception.BlockchainException;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.CipherException;

import java.io.IOException;

/**
 * @author 김승진
 * @description 블록체인 관련 라이브러리 Caver-java 관련 클래스들을 Spring Bean으로 등록하는 설정 클래스
 */

@Configuration
@RequiredArgsConstructor
public class BlockchainConfig {
    private final BlockchainProperties blockchainProperties;
    private static final String BAOBAB_TESTNET = "https://api.baobab.klaytn.net:8651";

    @Bean
    public AbstractKeyring keyring() {
        try {
            return KeyringFactory.decrypt(blockchainProperties.getKeyring(), blockchainProperties.getPassword());
        } catch (CipherException | IOException e) {
            throw new BlockchainException();
        }
    }

    @Bean
    public Caver caver() {
        return new Caver(BAOBAB_TESTNET);
    }

    @Bean
    public Contract contract() {
        try {
            return caver().contract.create(blockchainProperties.getAbi());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }
}
