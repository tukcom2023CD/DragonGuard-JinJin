package com.dragonguard.backend.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 김승진
 * @description OAuth2를 위한 임시 컨트롤러
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping
    public ResponseEntity<Object> authorize(@RequestParam Object code) {
        
        return ResponseEntity.ok(code);
    }
}
