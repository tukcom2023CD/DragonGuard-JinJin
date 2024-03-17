package com.dragonguard.backend.global.exception;

/**
 * @author 김승진
 * @description API 토큰의 횟수 초과로 인해 나타내는 예외 클래스
 */
public class NoApiTokenException extends IllegalStateException {
    private static final String MESSAGE = "API 토큰의 시간당 횟수를 초과했습니다.";

    public NoApiTokenException() {
        super(MESSAGE);
    }
}
