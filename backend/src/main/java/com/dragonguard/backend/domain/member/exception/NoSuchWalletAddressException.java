package com.dragonguard.backend.domain.member.exception;

/**
 * @author 김승진
 * @description 지갑 주소 예외에 던져질 Exception
 */
public class NoSuchWalletAddressException extends IllegalArgumentException {
    private static final String MESSAGE = "멤버의 지갑 주소가 없습니다.";

    public NoSuchWalletAddressException() {
        super(MESSAGE);
    }
}
