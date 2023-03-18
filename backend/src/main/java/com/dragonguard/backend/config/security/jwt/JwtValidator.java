package com.dragonguard.backend.config.security.jwt;

import com.dragonguard.backend.config.security.oauth.user.UserDetailsImpl;
import com.dragonguard.backend.config.security.oauth.user.UserDetailsMapper;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.UUID;

/**
 * @author 김승진
 * @description JWT 토큰 정보를 검증하는 클래스
 */

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final Key key;
    private final MemberService memberService;
    private final UserDetailsMapper userDetailsMapper;

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getTokenBodyClaims(accessToken);
        Member member = memberService.getEntity(extractUUID(claims));
        UserDetailsImpl loginUser = userDetailsMapper.mapToLoginUser(member);

        return new UsernamePasswordAuthenticationToken(loginUser, "", loginUser.getAuthorities());
    }

    private UUID extractUUID(Claims claims) {
        return UUID.fromString(claims.get("id", String.class));
    }

    private Claims getTokenBodyClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
