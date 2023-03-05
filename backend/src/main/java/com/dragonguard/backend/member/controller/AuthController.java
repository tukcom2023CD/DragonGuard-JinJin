package com.dragonguard.backend.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 김승진
 * @description OAuth2를 위한 임시 컨트롤러
 */

@RestController
public class AuthController {

    @GetMapping("/github/callback")
    public ResponseEntity<String> authorize(@RequestParam String code) {
        return ResponseEntity.ok(code);
    }
}
