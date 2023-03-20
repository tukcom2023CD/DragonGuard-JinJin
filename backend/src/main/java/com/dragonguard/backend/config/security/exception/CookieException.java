package com.dragonguard.backend.config.security.exception;

public class CookieException extends IllegalStateException {
    private static final String MESSAGE = "Cookie 로직 중 예외가 발생했습니다.";

    public CookieException() {
        super(MESSAGE);
    }
}
