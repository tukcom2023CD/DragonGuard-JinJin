package com.dragonguard.backend.global.exception;

public class ScrapingRequestFailException extends IllegalStateException {
    private static final String MESSAGE = "스크래핑 모듈과 통신에 실패했습니다.";

    public ScrapingRequestFailException() {
        super(MESSAGE);
    }
}
