package com.dragonguard.backend.global.exception;

public abstract class BusinessException extends IllegalStateException {
    public BusinessException(String message) {
        super(message);
    }
}
