package com.dragonguard.backend.member.exception;

/**
 * @author 김승진
 * @description OAuth2 로직중 생기는 예외에 던져질 Exception
 */

public class OAuthProcessingException extends IllegalStateException {
    private static final String MESSAGE = "OAuth2 로직 중 오류가 발생했습니다.";

    public OAuthProcessingException() {
        super(MESSAGE);
    }
}
