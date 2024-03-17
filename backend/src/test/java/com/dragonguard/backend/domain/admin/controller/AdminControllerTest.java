package com.dragonguard.backend.domain.admin.controller;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.admin.service.AdminService;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.support.docs.RestDocumentTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@DisplayName("admin 컨트롤러의")
@WebMvcTest(AdminController.class)
class AdminControllerTest extends RestDocumentTest {
    @MockBean private AdminService adminService;

    @Test
    @DisplayName("관리자인지 확인이 수행되는가")
    void checkAdmin() throws Exception {
        ResultActions perform =
                mockMvc.perform(
                        get("/admin/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("check admin", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("조직 요청 승인 혹은 반려가 수행되는가")
    void decideRequest() throws Exception {
        List<AdminOrganizationResponse> expected =
                List.of(
                        new AdminOrganizationResponse(
                                2L, "한국공학대학교", OrganizationType.UNIVERSITY, "tukorea.ac.kr"),
                        new AdminOrganizationResponse(
                                3L, "Google", OrganizationType.COMPANY, "gmail.com"));
        given(adminService.decideRequestedOrganization(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        post("/admin/organizations/decide")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                new AdminDecideRequest(
                                                        1L, OrganizationStatus.ACCEPTED)))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk()).andExpect(jsonPath("$").isArray());

        perform.andDo(print())
                .andDo(
                        document(
                                "accept or deny organization request",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    @DisplayName("조직 상태에 따른 조회가 수행되는가")
    void getOrganizationsByStatus() throws Exception {
        List<AdminOrganizationResponse> expected =
                List.of(
                        new AdminOrganizationResponse(
                                2L, "한국공학대학교", OrganizationType.UNIVERSITY, "tukorea.ac.kr"),
                        new AdminOrganizationResponse(
                                3L, "Google", OrganizationType.COMPANY, "gmail.com"));
        given(adminService.findOrganizationsByStatus(any(), any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/admin/organizations?status=REQUESTED&page=0&size=20")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk()).andExpect(jsonPath("$").isArray());

        perform.andDo(print())
                .andDo(
                        document(
                                "get organization list with admin authority",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }
}
