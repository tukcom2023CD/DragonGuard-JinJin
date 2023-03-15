package com.dragonguard.backend.config.security.oauth;

public class OAuthProcessingException extends IllegalStateException {
    private static final String MESSAGE = "OAuth2 로직 중 오류가 발생했습니다.";

    public OAuthProcessingException() {
        super(MESSAGE);
    }
}
