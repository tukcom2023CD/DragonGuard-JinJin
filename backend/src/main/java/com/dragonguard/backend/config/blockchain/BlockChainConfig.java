package com.dragonguard.backend.config.blockchain;

import com.dragonguard.backend.global.exception.BlockchainException;
import com.klaytn.caver.Caver;
import com.klaytn.caver.kct.kip7.KIP7;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import kotlin.text.Charsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class BlockChainConfig {

    private final BlockChainProperties properties;

    @Bean
    public Caver caver() {
        return new Caver();
    }

    @Bean
    public String abiJson() {
        Path path = Paths.get(Paths.get("src/main/resources/").toAbsolutePath().normalize()
                .resolve("DragonContract.json")
                .normalize()
                .toString());
        try {
            return Files.readAllLines(path, Charsets.UTF_8).stream().collect(Collectors.joining());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }

    @Bean
    public SingleKeyring deployer() {
        return KeyringFactory.createFromPrivateKey(properties.getPrivateKey());
    }

    @Bean
    public KIP7 kip7() {
        try {
            return KIP7.create(caver(), properties.getUrl());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }
}
