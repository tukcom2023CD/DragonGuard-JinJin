package com.dragonguard.backend.global.advice;

import com.dragonguard.backend.blockchain.exception.BlockchainException;
import com.dragonguard.backend.email.exception.EmailException;
import com.dragonguard.backend.member.exception.CookieException;
import com.dragonguard.backend.member.exception.JwtProcessingException;
import com.dragonguard.backend.member.exception.OAuthProcessingException;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.exception.TierNoneMatchException;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.ValidationException;

/**
 * @author 김승진
 * @description API 요청을 받아 로직 수행중 나타날 에러에 대한 처리를 담당하는 클래스
 */

@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(javax.persistence.EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> webClientResponse(WebClientResponseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ErrorResponse> decodingException(DecodingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(CookieException.class)
    public ResponseEntity<ErrorResponse> cookieException(CookieException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(JwtProcessingException.class)
    public ResponseEntity<ErrorResponse> jwtProcessingException(JwtProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(OAuthProcessingException.class)
    public ResponseEntity<ErrorResponse> oAuthProcessingException(OAuthProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorResponse> emailException(EmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(TierNoneMatchException.class)
    public ResponseEntity<ErrorResponse> tierNoneMatchException(TierNoneMatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BlockchainException.class)
    public ResponseEntity<ErrorResponse> blockchainException(BlockchainException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
}
