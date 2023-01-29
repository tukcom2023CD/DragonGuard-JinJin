package com.dragonguard.backend.global.exception;

public class EntityNotFoundException extends BusinessException {
    private static final String MESSAGE = "엔티티 조회에 실패했습니다.";

    public EntityNotFoundException() {
        super(MESSAGE);
    }
}
