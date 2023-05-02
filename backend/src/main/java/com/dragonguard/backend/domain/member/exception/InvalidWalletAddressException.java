package com.dragonguard.backend.domain.member.exception;

public class InvalidWalletAddressException extends IllegalArgumentException {
    private static final String MESSAGE = "잘못된 지갑 주소입니다.";

    public InvalidWalletAddressException() {
        super(MESSAGE);
    }
}
