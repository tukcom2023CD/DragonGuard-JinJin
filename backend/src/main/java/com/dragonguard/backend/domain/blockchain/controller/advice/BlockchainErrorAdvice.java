package com.dragonguard.backend.domain.blockchain.controller.advice;

import com.dragonguard.backend.domain.blockchain.exception.BlockchainException;
import com.dragonguard.backend.global.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BlockchainErrorAdvice {
    @ExceptionHandler(BlockchainException.class)
    public ResponseEntity<ErrorResponse> blockchainException(BlockchainException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
}
