package com.dragonguard.backend.config.security.oauth;

import lombok.Getter;

import java.util.Optional;

@Getter
public class OAuth2Request {
    private String accountId;
    private Optional<String> name;

    public OAuth2Request(String accountId, String name) {
        this.accountId = accountId;
        this.name = Optional.ofNullable(name);
    }
}
