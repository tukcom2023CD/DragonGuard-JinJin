package com.dragonguard.backend.domain.email.exception;

public class EmailException extends IllegalStateException {
    private static final String MESSAGE = "이메일 로직 오류가 발생했습니다.";

    public EmailException() {
        super(MESSAGE);
    }
}
