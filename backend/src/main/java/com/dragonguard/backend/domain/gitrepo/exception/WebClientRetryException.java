package com.dragonguard.backend.domain.gitrepo.exception;

public class WebClientRetryException extends IllegalStateException {
    private static final String MESSAGE = "재시도를 수행해도 데이터를 받지 못했습니다.";

    public WebClientRetryException() {
        super(MESSAGE);
    }
}
