package com.dragonguard.backend.domain.member.controller;

import com.dragonguard.backend.domain.member.dto.response.MemberGitOrganizationRepoResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberGitReposAndGitOrganizationsResponse;
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

@DisplayName("member 컨트롤러의")
@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocumentTest {
    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("멤버 생성이 수행되는가")
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
                                                MemberRequestFixture.OHKSJ77
                                                        .toMemberRequest()))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isCreated());

        // docs
        perform.andDo(print())
                .andDo(document("create member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 기여도 내역 업데이트가 수행되는가")
    void updateContributions() throws Exception {
        // given

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/contributions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isNoContent());

        // docs
        perform.andDo(print())
                .andDo(document("update member contributions", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 기여도 내역 업데이트가 수행되는가")
    void updateBlockchains() throws Exception {
        // given

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/blockchains")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isNoContent());

        // docs
        perform.andDo(print())
                .andDo(document("update member blockchains", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 조회가 수행되는가")
    void getMember() throws Exception {
        // given
        MemberResponse expected = new MemberResponse(UUID.randomUUID(), "김승진", "ohksj77", 100, 100, 100, 100, Tier.SILVER, AuthStep.GITHUB_ONLY, "http://abcd.efgh", 1000, 1000L, "한국공학대학교", "http://abcd.efgh", 1, true, List.of("ohksj", "ksj77"));
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
    @DisplayName("멤버 조회가 수행되는가")
    void getMemberDetails() throws Exception {
        // given
        MemberGitReposAndGitOrganizationsResponse expected = new MemberGitReposAndGitOrganizationsResponse(List.of("tukcom2023CD"), List.of("DragonGuard-JinJin", "DragonGuard", "Jin-Jin"));
        given(memberService.findMemberDetailByGithubId(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members?githubId=ohksj77")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("get member details", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 티어 조회가 수행되는가")
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
    @DisplayName("멤버 전체 랭킹 조회가 수행되는가")
    void getRanking() throws Exception {
        // given
        List<MemberRankResponse> expected = List.of(
                new MemberRankResponse(UUID.randomUUID(), "Kim", "ohksj77", 10000L, Tier.MASTER, "http://github123123412412412profileUrl"),
                new MemberRankResponse(UUID.randomUUID(), "Seung", "ohksj", 5000L, Tier.RUBY, "http://github123123412412412profileUrl"),
                new MemberRankResponse(UUID.randomUUID(), "Jin", "ohksj777", 3000L, Tier.DIAMOND, "http://github123123412412412profileUrl"),
                new MemberRankResponse(UUID.randomUUID(), "Lee", "ohksjj", 1000L, Tier.PLATINUM, "http://github123123412412412profileUrl"),
                new MemberRankResponse(UUID.randomUUID(), "Da", "ohksjksj", 500L, Tier.GOLD, "http://github123123412412412profileUrl"));
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
    @DisplayName("멤버 지갑 주소 갱신이 수행되는가")
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
        perform.andExpect(status().isNoContent());

        // docs
        perform.andDo(print())
                .andDo(document("update member wallet", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("Jwt를 통한 멤버 조회가 수행되는가")
    void getMemberWithJwt() throws Exception {
        // given
        MemberResponse expected = new MemberResponse(UUID.randomUUID(), "김승진", "ohksj77", 100, 100, 100, 100, Tier.SILVER, AuthStep.GITHUB_ONLY, "http://abcd.efgh", 1000, 1000L, "한국공학대학교", "http://abcd.efgh", 1, true, List.of("ohksj", "ksj77"));
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
    @DisplayName("조직 내부 멤버 랭킹 조회가 수행되는가")
    void getOrganizationMemberRank() throws Exception {
        // given
        List<MemberRankResponse> expected = List.of(
                new MemberRankResponse(UUID.randomUUID(), "정해진", "HJ39", 20L, Tier.SPROUT, "http://githubUserProfileImageUrl"),
                new MemberRankResponse(UUID.randomUUID(), "넓은관용", "Sammuelwoojae", 20L, Tier.SPROUT, "http://githubUserProfileImageUrl"),
                new MemberRankResponse(UUID.randomUUID(), "회사승진", "ohksj77", 20L, Tier.SPROUT, "http://githubUserProfileImageUrl"),
                new MemberRankResponse(UUID.randomUUID(), "영어수학", "posite", 20L, Tier.SPROUT, "http://githubUserProfileImageUrl"));
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

    @Test
    @DisplayName("멤버의 조직에 소속된 레포 조회가 수행되는가")
    void getMemberGitOrganizationRepo() throws Exception {
        // given
        List<MemberGitOrganizationRepoResponse> expected = List.of(
                new MemberGitOrganizationRepoResponse("tukcom2023CD/DragonGuard-JinJin"),
                new MemberGitOrganizationRepoResponse("tukcom2023CD/DragonGuard"),
                new MemberGitOrganizationRepoResponse("tukcom2023CD/Yongari"));
        given(memberService.getMemberGitOrganizationRepo(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/git-organizations/git-repos?name=tukcom2023CD")
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("get member repositories in a organization", getDocumentRequest(), getDocumentResponse()));
    }
}
