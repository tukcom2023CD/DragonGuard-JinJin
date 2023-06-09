package com.dragonguard.backend.config.properties;

import com.dragonguard.backend.config.blockchain.BlockchainProperties;
import com.dragonguard.backend.config.github.GithubProperties;
import com.dragonguard.backend.config.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({GithubProperties.class, BlockchainProperties.class, RedisProperties.class})
public class PropertiesConfig {}
