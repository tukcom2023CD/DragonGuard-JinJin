package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.service.MemberService;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import com.dragonguard.backend.support.fixture.member.dto.MemberRequestFixture;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocumentTest {

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("멤버 생성")
    void savemember() throws Exception {
        // given
        Long expected = 1L;
        given(memberService.saveMember(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/api/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                MemberRequestFixture.SAMPLE1
                                                        .toMemberRequest())));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expected));

        // docs
        perform.andDo(print())
                .andDo(document("create member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 커밋 내역 업데이트")
    void updatecommits() throws Exception {
        // given

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/api/members/1/commits")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("update member commits", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 조회")
    void getmember() throws Exception {
        // given
        MemberResponse expected = new MemberResponse(1L, "김승진", "ohksj77", Tier.SILVER, AuthStep.NONE);
        given(memberService.getMember(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/api/members/1")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.githubId").value("ohksj77"));

        // docs
        perform.andDo(print())
                .andDo(document("get member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 티어 조회")
    void gettier() throws Exception {
        // given
        Tier expected = Tier.MASTER;
        given(memberService.getTier(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/api/members/1/tier")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(Tier.MASTER.toString()));

        // docs
        perform.andDo(print())
                .andDo(document("get member tier", getDocumentRequest(), getDocumentResponse()));
    }
}
