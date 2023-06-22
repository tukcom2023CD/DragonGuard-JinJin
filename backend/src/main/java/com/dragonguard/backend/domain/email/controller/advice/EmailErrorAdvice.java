package com.dragonguard.backend.domain.email.controller.advice;

import com.dragonguard.backend.domain.email.exception.EmailException;
import com.dragonguard.backend.global.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 김승진
 * @description 이메일 요청에 대한 예외를 처리하는 Controller Advice
 */

@RestControllerAdvice
public class EmailErrorAdvice {
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorResponse> emailException(EmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
}
