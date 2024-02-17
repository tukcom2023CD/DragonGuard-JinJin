package com.dragonguard.backend.domain.member.controller.advice;

import com.dragonguard.backend.domain.member.exception.InvalidGithubTokenException;
import com.dragonguard.backend.domain.member.exception.JwtProcessingException;
import com.dragonguard.backend.domain.member.exception.NoSuchWalletAddressException;
import com.dragonguard.backend.global.advice.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 김승진
 * @description 멤버 관련 요청에서 나타날 수 있는 예외를 처리하는 ControllerAdvice 클래스
 */
@RestControllerAdvice
public class MemberErrorAdvice {
    @ExceptionHandler(JwtProcessingException.class)
    public ResponseEntity<ErrorResponse> jwtProcessingException(final JwtProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> jwtExpiredException(final ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidGithubTokenException.class)
    public ResponseEntity<ErrorResponse> invalidGithubTokenException(
            final InvalidGithubTokenException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchWalletAddressException.class)
    public ResponseEntity<ErrorResponse> noSuchWalletAddressException(
            final NoSuchWalletAddressException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }
}
