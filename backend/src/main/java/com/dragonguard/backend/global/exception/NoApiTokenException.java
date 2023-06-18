package com.dragonguard.backend.global.exception;

public class NoApiTokenException extends IllegalStateException {
    private static final String MESSAGE = "API 토큰의 시간당 횟수를 초과했습니다.";

    public NoApiTokenException() {
        super(MESSAGE);
    }
}
