package com.dragonguard.backend.domain.member.exception;

/**
 * @author 김승진
 * @description Cookie 로직중 생기는 예외에 던져질 Exception
 */

public class CookieException extends IllegalStateException {
    private static final String MESSAGE = "Cookie 로직 중 예외가 발생했습니다.";

    public CookieException() {
        super(MESSAGE);
    }
}
