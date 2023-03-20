package com.dragonguard.backend.global.advice;

import com.dragonguard.backend.config.security.exception.CookieException;
import com.dragonguard.backend.config.security.exception.JwtProcessingException;
import com.dragonguard.backend.config.security.exception.OAuthProcessingException;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * @author 김승진
 * @description API 요청을 받아 로직 수행중 나타날 에러에 대한 처리를 담당하는 클래스
 */

@Slf4j
@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("엔티티를 찾을 수 없습니다.");
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(javax.persistence.EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("엔티티를 찾을 수 없습니다.");
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> webClientResponse(WebClientResponseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("통신 오류가 발생했습니다.");
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<String> decodingException(DecodingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("디코딩 에러가 발생했습니다.");
    }

    @ExceptionHandler(CookieException.class)
    public ResponseEntity<String> decodingException(CookieException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("쿠키 에러가 발생했습니다.");
    }

    @ExceptionHandler(JwtProcessingException.class)
    public ResponseEntity<String> decodingException(JwtProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 에러가 발생했습니다.");
    }

    @ExceptionHandler(OAuthProcessingException.class)
    public ResponseEntity<String> decodingException(OAuthProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OAuth 에러가 발생했습니다.");
    }
}
