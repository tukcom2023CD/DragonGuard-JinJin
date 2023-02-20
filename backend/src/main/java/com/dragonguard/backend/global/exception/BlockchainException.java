package com.dragonguard.backend.global.exception;

public class BlockchainException extends IllegalStateException {
    private static final String MESSAGE = "블록체인 로직에 오류가 발생했습니다.";

    public BlockchainException() {
        super(MESSAGE);
    }
}
