package com.dragonguard.backend.config.blockchain;

import com.klaytn.caver.Caver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BlockChainConfig {
    @Bean
    public Caver caver() {
        return new Caver("https://api.baobab.klaytn.net:8651");
    }
}
