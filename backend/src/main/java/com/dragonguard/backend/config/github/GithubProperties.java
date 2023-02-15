package com.dragonguard.backend.config.github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "github")
@Component
public class GithubProperties {
    private String url;
    private String token;
}
