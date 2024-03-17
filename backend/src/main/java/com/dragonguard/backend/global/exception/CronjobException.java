package com.dragonguard.backend.global.exception;

/**
 * @author 김승진
 * @description 크론잡 실행 중 발생하는 예외
 */
public class CronjobException extends IllegalStateException {

    private static final String MESSAGE = "크론잡 실행 중 예외가 발생했습니다.";

    public CronjobException() {
        super(MESSAGE);
    }
}
