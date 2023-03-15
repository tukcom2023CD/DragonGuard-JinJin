package com.dragonguard.backend.config.security.oauth;

import lombok.Getter;

import java.util.Optional;

@Getter
public class OAuth2Request {
    private String accountId;
    private Optional<String> name;
    private Optional<String> email;

    public OAuth2Request(String accountId, String name, String email) {
        this.accountId = accountId;
        this.name = Optional.ofNullable(name);
        this.email = Optional.ofNullable(email);
    }
}
