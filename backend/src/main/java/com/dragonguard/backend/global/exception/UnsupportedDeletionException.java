package com.dragonguard.backend.global.exception;

public class UnsupportedDeletionException extends IllegalStateException {
    private static final String MESSAGE = "논리적 삭제만 허용합니다.";

    public UnsupportedDeletionException() {
        super(MESSAGE);
    }
}
