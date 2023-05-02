package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestHeader String refreshToken,
            @RequestHeader String accessToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken, accessToken));
    }
}
