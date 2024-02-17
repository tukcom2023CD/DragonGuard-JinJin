package com.dragonguard.backend.global.exception;

/**
 * @author 김승진
 * @description hard delete 시도시 나타나는 예외
 */
public class UnsupportedDeletionException extends IllegalStateException {
    private static final String MESSAGE = "논리적 삭제만 허용합니다.";

    public UnsupportedDeletionException() {
        super(MESSAGE);
    }
}
