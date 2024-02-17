package com.dragonguard.backend.domain.email.exception;

/**
 * @author 김승진
 * @description 이메일의 로직 처리 중 나타나는 오류를 나타내는 클래스
 */
public class EmailException extends IllegalStateException {
    private static final String MESSAGE = "이메일 로직 오류가 발생했습니다.";

    public EmailException() {
        super(MESSAGE);
    }
}
