package com.dragonguard.backend.global.exception;

/**
 * @author 김승진
 * @description Github REST API와의 통신에서 나타나는 4xx 코드에 대한 예외
 */
public class ClientBadRequestException extends IllegalStateException {
    private static final String MESSAGE = "클라이언트의 4xx 오류가 발생했습니다.";

    public ClientBadRequestException() {
        super(MESSAGE);
    }
}
