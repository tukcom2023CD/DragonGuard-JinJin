package com.dragonguard.backend.global.advice;

import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFound(EntityNotFoundException e) {
        log.info("entity not found exception occured");
        return ResponseEntity.ok("엔티티를 찾을 수 없습니다.");
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(javax.persistence.EntityNotFoundException e) {
        log.info("entity not found exception occured");
        return ResponseEntity.ok("엔티티를 찾을 수 없습니다.");
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<List> webClientResponse(WebClientResponseException e) {
        log.info("web client error occured");
        return ResponseEntity.ok(List.of());
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<List> decodingException(DecodingException e) {
        log.info("decoding error occured");
        return ResponseEntity.ok(List.of());
    }
}
