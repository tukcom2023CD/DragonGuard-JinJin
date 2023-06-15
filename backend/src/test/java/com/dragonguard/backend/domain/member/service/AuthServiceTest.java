package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DatabaseTest
@DisplayName("Auth 서비스의")
class AuthServiceTest extends LoginTest {

    @Test
    @DisplayName("리프레시 토큰 갱신 기능이 수행되는가")
    void refreshToken() {
        //given
        JwtToken expected = new JwtToken("accessToken", "refreshToken", "Bearer");
        when(authService.refreshToken(any(), any())).thenReturn(expected);

        //when
        JwtToken result = authService.refreshToken(loginUser.getRefreshToken(), "I am a access token");

        //then
        assertThat(result).isEqualTo(expected);
    }
}
