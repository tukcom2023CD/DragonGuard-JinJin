package com.dragonguard.backend.config.security.exception;

public class JwtProcessingException extends IllegalStateException {
    private static final String MESSAGE = "JWT 토큰 로직 중 예외가 발생했습니다.";

    public JwtProcessingException() {
        super(MESSAGE);
    }
}
