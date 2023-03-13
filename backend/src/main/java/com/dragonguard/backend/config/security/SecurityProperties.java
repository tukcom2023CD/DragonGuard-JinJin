package com.dragonguard.backend.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.github")
@Component
public class SecurityProperties {
    private String clientId;
    private String clientSecret;
}
