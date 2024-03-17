package com.dragonguard.backend.domain.member.controller;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 김승진
 * @description 토큰 재발급을 위한 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<JwtToken> authorize(
            @RequestHeader final String refreshToken, @RequestHeader final String accessToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken, accessToken));
    }
}
