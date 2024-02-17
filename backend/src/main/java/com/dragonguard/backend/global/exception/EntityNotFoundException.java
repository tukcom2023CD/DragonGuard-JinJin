package com.dragonguard.backend.global.exception;

/**
 * @author 김승진
 * @description 엔티티 조회에 실패했을 때 나타나는 예외
 */
public class EntityNotFoundException extends IllegalStateException {
    private static final String MESSAGE = "엔티티 조회에 실패했습니다.";

    public EntityNotFoundException() {
        super(MESSAGE);
    }
}
