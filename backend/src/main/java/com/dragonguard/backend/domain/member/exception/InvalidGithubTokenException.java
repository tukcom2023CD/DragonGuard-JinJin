package com.dragonguard.backend.domain.member.exception;

/**
 * @author 김승진
 * @description 깃허브 토큰 예외에 던져질 Exception
 */

public class InvalidGithubTokenException extends IllegalArgumentException {
    private static final String MESSAGE = "깃허브 토큰이 잘못되었습니다.";

    public InvalidGithubTokenException() {
        super(MESSAGE);
    }
}
