package com.dragonguard.backend.email.controller;

import com.dragonguard.backend.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.email.service.EmailService;
import com.dragonguard.backend.global.IdResponse;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
class EmailControllerTest extends RestDocumentTest {
    @MockBean
    private EmailService emailService;

    @Test
    @DisplayName("이메일 전송")
    void sendEmail() throws Exception {
        IdResponse<Long> expected = new IdResponse<>(1L);
        given(emailService.sendEmail()).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        post("/email/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("send email", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("메일 코드 확인")
    void checkCode() throws Exception {
        CheckCodeResponse expected = new CheckCodeResponse(true);
        given(emailService.isCodeMatching(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/email/check?id=1&code=12345")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("validate email code", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("이메일 코드 삭제")
    void deleteCode() throws Exception {
        IdResponse<Long> expected = new IdResponse<>(1L);
        given(emailService.sendEmail()).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        delete("/email/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("delete email code", getDocumentRequest(), getDocumentResponse()));
    }
}
