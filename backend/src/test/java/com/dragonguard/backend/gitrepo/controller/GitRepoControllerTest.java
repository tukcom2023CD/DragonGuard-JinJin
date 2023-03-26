package com.dragonguard.backend.gitrepo.controller;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.gitrepo.dto.response.*;
import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.dragonguard.backend.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

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

@WebMvcTest(GitRepoController.class)
class GitRepoControllerTest extends RestDocumentTest {

    @MockBean
    private GitRepoService gitRepoService;

    @Test
    @DisplayName("레포 멤버 조회")
    void getRepoMembers() throws Exception {
        List<GitRepoMemberResponse> expected = List.of(
                new GitRepoMemberResponse("ohksj77", 100, 1000, 500),
                new GitRepoMemberResponse("HJ39", 101, 999, 500),
                new GitRepoMemberResponse("posite", 99, 1001, 500),
                new GitRepoMemberResponse("Sammuelwoojae", 100, 1001, 499));
        given(gitRepoService.findMembersByGitRepoWithClient(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/git-repos?name=tukcom2023CD/DragonGuard-JinJin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        perform.andDo(print())
                .andDo(document("get git-repo contributors", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("두 레포 기여자 기여도 비교")
    void getTwoGitRepos() throws Exception {
        TwoGitRepoMemberResponse expected = new TwoGitRepoMemberResponse(List.of(
                new GitRepoMemberResponse("ohksj77", 100, 1000, 500),
                new GitRepoMemberResponse("HJ39", 101, 999, 500),
                new GitRepoMemberResponse("posite", 99, 1001, 500),
                new GitRepoMemberResponse("Sammuelwoojae", 100, 1001, 499)),
                List.of(
                        new GitRepoMemberResponse("ohksj77", 100, 1000, 500),
                        new GitRepoMemberResponse("HJ39", 101, 999, 500),
                        new GitRepoMemberResponse("posite", 99, 1001, 500),
                        new GitRepoMemberResponse("Sammuelwoojae", 100, 1001, 499)));
        given(gitRepoService.findMembersByGitRepoForCompare(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        post("/git-repos/compare/git-repos-members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                new GitRepoCompareRequest("tukcom2023CD/DragonGuard-JinJin", "tukcom2023CD/")))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("get comparing two git-repos members", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("두 레포 비교")
    void getGitRepoMembersForCompare() throws Exception {
        TwoGitRepoResponse expected = new TwoGitRepoResponse(
                new GitRepoResponse(new GitRepoClientResponse("tukcom2023CD/DragonGuard-JinJin", 1, 4, 4, 3, 23, 0),
                        new StatisticsResponse(new IntSummaryStatistics(4, 33, 146, 430),
                                new IntSummaryStatistics(4, 1800, 30000, 50000),
                                new IntSummaryStatistics(4, 5000, 15000, 30000)),
                        Map.of("java", 10000, "kotlin", 9999, "swift", 9998),
                        new IntSummaryStatistics(4, 9998, 10000, 29997)),
                new GitRepoResponse(new GitRepoClientResponse("tukcom2023CD/", 1, 4, 4, 3, 23, 0),
                        new StatisticsResponse(new IntSummaryStatistics(4, 33, 146, 430),
                                new IntSummaryStatistics(4, 1800, 30000, 50000),
                                new IntSummaryStatistics(4, 5000, 15000, 30000)),
                        Map.of("java", 10000, "kotlin", 9999, "swift", 9998),
                        new IntSummaryStatistics(4, 9998, 10000, 29997)));
        given(gitRepoService.findTwoGitRepos(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        post("/git-repos/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                new GitRepoCompareRequest("tukcom2023CD/DragonGuard-JinJin", "tukcom2023CD/")))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("get comparing two git-repos", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("두 멤버의 레포 기여도 비교")
    void getTwoGitRepoMember() throws Exception {
        GitRepoMemberCompareResponse expected = new GitRepoMemberCompareResponse(
                new GitRepoMemberResponse("ohksj77", 100, 1000, 500),
                new GitRepoMemberResponse("ohksj", 101, 1001, 501));
        given(gitRepoService.findTwoGitRepoMember(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        post("/git-repos/compare/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        toRequestBody(
                                                new GitRepoMemberCompareRequest("ohksj77", "tukcom2023CD/DragonGuard-JinJin", "ohksj", "tukcom2023CD/")))
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("get comparing two members contribution", getDocumentRequest(), getDocumentResponse()));
    }
}
