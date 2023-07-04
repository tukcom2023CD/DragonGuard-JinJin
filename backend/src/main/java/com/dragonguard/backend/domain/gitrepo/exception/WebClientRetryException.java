package com.dragonguard.backend.domain.gitrepo.exception;

public class WebClientRetryException extends IllegalStateException {
    private static final String MESSAGE = "";

    public WebClientRetryException() {
        super(MESSAGE);
    }
}
