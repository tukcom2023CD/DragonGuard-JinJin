package com.dragonguard.backend.blockchain.exception;

/**
 * @author 김승진
 * @description 블록체인 관련 오류 발생시 던질 예외 클래스
 */

public class BlockchainException extends IllegalStateException {
    private static final String MESSAGE = "블록체인 로직에 오류가 발생했습니다.";

    public BlockchainException() {
        super(MESSAGE);
    }
}
