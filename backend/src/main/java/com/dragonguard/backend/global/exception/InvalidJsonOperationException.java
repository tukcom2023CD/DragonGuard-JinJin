package com.dragonguard.backend.global.exception;

/**
 * @author 김승진
 * @description 잘못된 json 연산에 대한 예외 처리 클래스
 */
public class InvalidJsonOperationException extends IllegalStateException {

    private static final String MESSAGE = "잘못된 json 연산입니다.";

    public InvalidJsonOperationException() {
        super(MESSAGE);
    }
}
