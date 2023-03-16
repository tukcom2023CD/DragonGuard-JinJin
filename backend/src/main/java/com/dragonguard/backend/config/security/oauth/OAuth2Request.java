package com.dragonguard.backend.config.security.oauth;

import lombok.Getter;

@Getter
public class OAuth2Request {
    private String accountId;

    public OAuth2Request(String accountId) {
        this.accountId = accountId;
    }
}
