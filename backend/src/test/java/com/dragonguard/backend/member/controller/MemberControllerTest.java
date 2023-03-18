package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.member.service.MemberService;
import com.dragonguard.backend.support.DatabaseTest;
import com.dragonguard.backend.support.LoginTest;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import com.dragonguard.backend.support.fixture.member.dto.MemberRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DatabaseTest
@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocumentTest {
    @MockBean
    private MemberService memberService;
    @MockBean
    protected AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    protected Member loginUser;

    @BeforeEach
    public void setup() {
        Member member = new Member("Kim", "ohksj77", new Commit(2023, 100, "ohksj77"), "12341234", "https://github");
        loginUser = memberRepository.save(member);
        when(authService.getLoginUser()).thenReturn(loginUser);
    }

    @Test
    @DisplayName("멤버 생성")
    void saveMember() throws Exception {
        // given
        IdResponse<UUID> expected = new IdResponse<>(UUID.randomUUID());
        given(memberService.saveMember(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                MemberRequestFixture.SAMPLE1
                                                        .toMemberRequest())));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()));

        // docs
        perform.andDo(print())
                .andDo(document("create member", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 커밋 내역 업데이트")
    void updateCommits() throws Exception {
        // given

        // when
        ResultActions perform =
                mockMvc.perform(
                        post("/members/commits")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("update member commits", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("멤버 조회")
    void getMember() throws Exception {
        // given
        MemberResponse expected = new MemberResponse(UUID.randomUUID(), "김승진", "ohksj77", 100, Tier.SILVER, AuthStep.NONE, "http://abcd.efgh", 1000, 1000L);
        given(memberService.getMember()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/me")
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
    void getTier() throws Exception {
        // given
        Tier expected = Tier.MASTER;
        given(memberService.getTier()).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/members/tier")
                                .contentType(MediaType.APPLICATION_JSON));

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
                                .contentType(MediaType.APPLICATION_JSON));

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
                                                new WalletRequest("asdfasdf12341234"))));

        // then
        perform.andExpect(status().isOk());

        // docs
        perform.andDo(print())
                .andDo(document("update member wallet", getDocumentRequest(), getDocumentResponse()));
    }
}
