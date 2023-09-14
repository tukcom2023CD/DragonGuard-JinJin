package com.dragonguard.backend.domain.blockchain.controller.advice;

import com.dragonguard.backend.domain.blockchain.exception.BlockchainException;
import com.dragonguard.backend.global.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 김승진
 * @description 블록체인 관련 요청에서 나올 수 있는 예외들을 처리하는 Controller Advice
 */

@RestControllerAdvice
public class BlockchainErrorAdvice {
    @ExceptionHandler(BlockchainException.class)
    public ResponseEntity<ErrorResponse> blockchainException(final BlockchainException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
}
