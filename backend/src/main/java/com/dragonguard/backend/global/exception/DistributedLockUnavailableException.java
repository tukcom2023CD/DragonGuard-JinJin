package com.dragonguard.backend.global.exception;

public class DistributedLockUnavailableException extends IllegalStateException {
    private static final String MESSAGE = "중복 요청으로 인해 요청을 처리할 수 없습니다.";

    public DistributedLockUnavailableException() {
        super(MESSAGE);
    }
}
