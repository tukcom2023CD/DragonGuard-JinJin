package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.config.security.jwt.JwtToken;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocumentTest {
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("리프레시 토큰 갱신")
    void authorize() throws Exception {
        // given
        JwtToken jwtToken = new JwtToken("1234.1234.1234", "4321.4321.4321", "bearer");
        given(authService.refreshToken(any(), any())).willReturn(jwtToken);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("refreshToken", "apfawfawfa.awfsfawef2.r4svfv32")
                                .header("accessToken", "4321.4321.4321"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("refresh jwt token", getDocumentRequest(), getDocumentResponse()));
    }
}