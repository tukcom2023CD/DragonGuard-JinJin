package com.dragonguard.backend.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/github/callback")
    public ResponseEntity<String> authorize(@RequestParam String code) {
        return ResponseEntity.ok(code);
    }
}
