package com.dragonguard.backend.domain.member.controller;

import com.dragonguard.backend.domain.member.controller.MemberController;
import com.dragonguard.backend.domain.member.dto.response.MemberDetailResponse;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Tier;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import com.dragonguard.backend.support.fixture.member.dto.MemberRequestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
    void saveMember() throws Exception {
        // given
        IdResponse<UUID> expected = new IdResponse<>(UUID.randomUUID());
        given(memberService.saveMember(any(), any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                MemberRequestFixture.SAMPLE1
                                                        .toMemberRequest()))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("create member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 기여도 내역 업데이트")
    void updateContributions() throws Exception {
        // given

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/contributions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("update member contributions", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 기여도 내역 업데이트")
    void updateBlockchains() throws Exception {
        // given

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/blockchains")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("update member blockchains", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 조회")
    void getMember() throws Exception {
        // given
        MemberResponse expected = new MemberResponse(UUID.randomUUID(), "김승진", "ohksj77", 100, 100, 100, 100, Tier.SILVER, AuthStep.GITHUB_ONLY, "http://abcd.efgh", 1000, 1, 1000L, "한국공학대학교", "http://abcd.efgh");
        given(memberService.getMember()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.githubId").value("ohksj77"));

        // docs
        perform.andDo(print())
                .andDo(document("get member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 조회")
    void getMemberDetails() throws Exception {
        // given
        MemberDetailResponse expected = new MemberDetailResponse(UUID.randomUUID(), "김승진", "ohksj77", 100, 100, 100, 100, Tier.SILVER, AuthStep.GITHUB_ONLY, "http://abcd.efgh", 1000, 1, 1000L, "한국공학대학교", "http://abcd.efgh", List.of("tukcom2023CD"), List.of("DragonGuard-JinJin", "DragonGuard", "Jin-Jin"));
        given(memberService.findMemberDetailByGithubId(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members?githubId=ohksj77")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.githubId").value("ohksj77"));

        // docs
        perform.andDo(print())
                .andDo(document("get member details", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 티어 조회")
    void getTier() throws Exception {
        // given
        Tier expected = Tier.MASTER;
        given(memberService.getTier()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/tier")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(Tier.MASTER.toString()));

        // docs
        perform.andDo(print())
                .andDo(document("get member tier", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 전체 랭킹 조회")
    void getRanking() throws Exception {
        // given
        List<MemberRankResponse> expected = List.of(
                new MemberRankResponse(UUID.randomUUID(), "Kim", "ohksj77", 10000L, Tier.MASTER),
                new MemberRankResponse(UUID.randomUUID(), "Seung", "ohksj", 5000L, Tier.RUBY),
                new MemberRankResponse(UUID.randomUUID(), "Jin", "ohksj777", 3000L, Tier.DIAMOND),
                new MemberRankResponse(UUID.randomUUID(), "Lee", "ohksjj", 1000L, Tier.PLATINUM),
                new MemberRankResponse(UUID.randomUUID(), "Da", "ohksjksj", 500L, Tier.GOLD));
        given(memberService.getMemberRanking(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/ranking?page=0&size=20&sort=commits,DESC")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // docs
        perform.andDo(print())
                .andDo(document("get member ranking", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 지갑 주소 갱신")
    void updateWalletAddress() throws Exception {
        // given
        willDoNothing().given(memberService).updateWalletAddress(any());

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/wallet-address")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                new WalletRequest("asdfasdf12341234")))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("update member wallet", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 조회 with Jwt")
    void getMemberWithJwt() throws Exception {
        // given
        MemberResponse expected = new MemberResponse(UUID.randomUUID(), "김승진", "ohksj77", 100, 100, 100, 100, Tier.SILVER, AuthStep.GITHUB_ONLY, "http://abcd.efgh", 1000, 1, 1000L, "한국공학대학교", "http://abcd.efgh");
        given(memberService.getMember()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/me")
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("get member by jwt", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("조직 내부 멤버 랭킹")
    void getOrganizationMemberRank() throws Exception {
        // given
        List<MemberRankResponse> expected = List.of(
                new MemberRankResponse(UUID.randomUUID(), "정해진", "HJ39", 20L, Tier.SPROUT),
                new MemberRankResponse(UUID.randomUUID(), "넓은관용", "Sammuelwoojae", 20L, Tier.SPROUT),
                new MemberRankResponse(UUID.randomUUID(), "회사승진", "ohksj77", 20L, Tier.SPROUT),
                new MemberRankResponse(UUID.randomUUID(), "영어수학", "posite", 20L, Tier.SPROUT));
        given(memberService.getMemberRankingByOrganization(any(), any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/ranking/organization?organizationId=1&page=0&size=20")
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("get member ranking in a organization", getDocumentRequest(), getDocumentResponse()));
    }
}
