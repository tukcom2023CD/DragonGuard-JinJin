package com.dragonguard.backend.config.blockchain;

import com.dragonguard.backend.blockchain.exception.BlockchainException;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import io.ipfs.multibase.Charsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 블록체인 관련 라이브러리 Caver-java 관련 클래스들을 Spring Bean으로 등록하는 설정 클래스
 */

@Configuration
@RequiredArgsConstructor
public class BlockchainConfig {
    private final BlockchainProperties properties;
    private static final String BAOBAB_TESTNET = "https://api.baobab.klaytn.net:8651";

    @Bean
    public BlockchainJson blockchainJson() {
        Path path = Paths.get("src/main/resources/");
        Path abiPath = Paths.get(path.toAbsolutePath().normalize()
                .resolve("abi.json")
                .normalize()
                .toString());
        Path keyringPath = Paths.get(path.toAbsolutePath().normalize()
                .resolve("keyring.json")
                .normalize()
                .toString());

        try {
            String abiJson = Files.readAllLines(abiPath, Charsets.UTF_8).stream().collect(Collectors.joining());
            String keyRingJson = Files.readAllLines(keyringPath, Charsets.UTF_8).stream().collect(Collectors.joining());
            return new BlockchainJson(abiJson, keyRingJson);
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }

    @Bean
    public AbstractKeyring keyring() {
        try {
            return KeyringFactory.decrypt(blockchainJson().getKeyRingJson(), properties.getPassword());
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
            return caver().contract.create(blockchainJson().getAbiJson());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }
}
