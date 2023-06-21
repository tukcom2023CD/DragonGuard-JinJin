package com.dragonguard.backend.domain.gitrepo.controller;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.StatisticsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoService;
import com.dragonguard.backend.domain.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.TwoGitRepoMemberResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("gitrepo 컨트롤러의")
@WebMvcTest(GitRepoController.class)
class GitRepoControllerTest extends RestDocumentTest {

    @MockBean private GitRepoService gitRepoService;

    @Test
    @DisplayName("레포 멤버 조회가 수행되는가 (수동 업데이트)")
    void getRepoMembers() throws Exception {
        List<GitRepoMemberResponse> list = List.of(
                new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                new GitRepoMemberResponse("HJ39", "http://somethingProfileUrl", 101, 999, 500, true),
                new GitRepoMemberResponse("posite", "http://somethingProfileUrl", 99, 1001, 500, true),
                new GitRepoMemberResponse("Sammuelwoojae", "http://somethingProfileUrl", 100, 1001, 499, true));
        GitRepoResponse expected = new GitRepoResponse(List.of(1, 1, 1, 2, 3, 4, 5, 6, 24, 212, 32, 4), list);
        given(gitRepoService.findGitRepoInfos(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/git-repos?name=tukcom2023CD/DragonGuard-JinJin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("get git-repo contributors", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("레포 멤버 조회가 수행되는가")
    void getRepoMembersAndUpdate() throws Exception {
        List<GitRepoMemberResponse> list = List.of(
                new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                new GitRepoMemberResponse("HJ39", "http://somethingProfileUrl", 101, 999, 500, true),
                new GitRepoMemberResponse("posite", "http://somethingProfileUrl", 99, 1001, 500, true),
                new GitRepoMemberResponse("Sammuelwoojae", "http://somethingProfileUrl", 100, 1001, 499, true));
        GitRepoResponse expected = new GitRepoResponse(List.of(1, 1, 1, 2, 3, 4, 5, 6, 24, 212, 32, 4), list);
        given(gitRepoService.findGitRepoInfosAndUpdate(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/git-repos?name=tukcom2023CD/DragonGuard-JinJin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("get git-repo contributors for update", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("두 레포 기여자 기여도 비교가 수행되는가 (수동 업데이트)")
    void getTwoGitRepos() throws Exception {
        TwoGitRepoMemberResponse expected = new TwoGitRepoMemberResponse(List.of(
                new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                new GitRepoMemberResponse("HJ39", "http://somethingProfileUrl", 101, 999, 500, true),
                new GitRepoMemberResponse("posite", "http://somethingProfileUrl", 99, 1001, 500, true),
                new GitRepoMemberResponse("Sammuelwoojae", "http://somethingProfileUrl", 100, 1001, 499, true)),
                List.of(
                        new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                        new GitRepoMemberResponse("HJ39", "http://somethingProfileUrl", 101, 999, 500, true),
                        new GitRepoMemberResponse("posite","http://somethingProfileUrl", 99, 1001, 500, true),
                        new GitRepoMemberResponse("Sammuelwoojae", "http://somethingProfileUrl", 100, 1001, 499, true)));
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
    @DisplayName("두 레포 기여자 기여도 비교가 수행되는가")
    void getTwoGitReposForUpdate() throws Exception {
        TwoGitRepoMemberResponse expected = new TwoGitRepoMemberResponse(List.of(
                new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                new GitRepoMemberResponse("HJ39", "http://somethingProfileUrl", 101, 999, 500, true),
                new GitRepoMemberResponse("posite", "http://somethingProfileUrl", 99, 1001, 500, true),
                new GitRepoMemberResponse("Sammuelwoojae", "http://somethingProfileUrl", 100, 1001, 499, true)),
                List.of(
                        new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                        new GitRepoMemberResponse("HJ39", "http://somethingProfileUrl", 101, 999, 500, true),
                        new GitRepoMemberResponse("posite", "http://somethingProfileUrl", 99, 1001, 500, true),
                        new GitRepoMemberResponse("Sammuelwoojae", "http://somethingProfileUrl", 100, 1001, 499, true)));
        given(gitRepoService.findMembersByGitRepoForCompareAndUpdate(any())).willReturn(expected);

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
                .andDo(document("get comparing two git-repos members for update", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("두 레포 비교가 수행되는가 (수동 업데이트)")
    void getGitRepoMembersForCompare() throws Exception {
        TwoGitRepoResponse expected = new TwoGitRepoResponse(
                new GitRepoCompareResponse(new GitRepoClientResponse("tukcom2023CD/DragonGuard-JinJin", 1, 4, 4, 3, 23, 0),
                        new StatisticsResponse(new IntSummaryStatistics(4, 33, 146, 430),
                                new IntSummaryStatistics(4, 1800, 30000, 50000),
                                new IntSummaryStatistics(4, 5000, 15000, 30000)),
                        Map.of("java", 10000, "kotlin", 9999, "swift", 9998),
                        new IntSummaryStatistics(4, 9998, 10000, 29997),
                        List.of("http://profileImage", "http://profileImage")),
                new GitRepoCompareResponse(new GitRepoClientResponse("tukcom2023CD/", 1, 4, 4, 3, 23, 0),
                        new StatisticsResponse(new IntSummaryStatistics(4, 33, 146, 430),
                                new IntSummaryStatistics(4, 1800, 30000, 50000),
                                new IntSummaryStatistics(4, 5000, 15000, 30000)),
                        Map.of("java", 10000, "kotlin", 9999, "swift", 9998),
                        new IntSummaryStatistics(4, 9998, 10000, 29997),
                        List.of("http://profileImage", "http://profileImage")));
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
    @DisplayName("두 레포 비교가 수행되는가")
    void getGitRepoMembersForCompareForUpdate() throws Exception {
        TwoGitRepoResponse expected = new TwoGitRepoResponse(
                new GitRepoCompareResponse(new GitRepoClientResponse("tukcom2023CD/DragonGuard-JinJin", 1, 4, 4, 3, 23, 0),
                        new StatisticsResponse(new IntSummaryStatistics(4, 33, 146, 430),
                                new IntSummaryStatistics(4, 1800, 30000, 50000),
                                new IntSummaryStatistics(4, 5000, 15000, 30000)),
                        Map.of("java", 10000, "kotlin", 9999, "swift", 9998),
                        new IntSummaryStatistics(4, 9998, 10000, 29997),
                        List.of("http://profileImage", "http://profileImage")),
                new GitRepoCompareResponse(new GitRepoClientResponse("tukcom2023CD/", 1, 4, 4, 3, 23, 0),
                        new StatisticsResponse(new IntSummaryStatistics(4, 33, 146, 430),
                                new IntSummaryStatistics(4, 1800, 30000, 50000),
                                new IntSummaryStatistics(4, 5000, 15000, 30000)),
                        Map.of("java", 10000, "kotlin", 9999, "swift", 9998),
                        new IntSummaryStatistics(4, 9998, 10000, 29997),
                        List.of("http://profileImage", "http://profileImage")));
        given(gitRepoService.findTwoGitReposAndUpdate(any())).willReturn(expected);

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
                .andDo(document("get comparing two git-repos for update", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("두 멤버의 레포 기여도 비교가 수행되는가")
    void getTwoGitRepoMember() throws Exception {
        GitRepoMemberCompareResponse expected = new GitRepoMemberCompareResponse(
                new GitRepoMemberResponse("ohksj77", "http://somethingProfileUrl", 100, 1000, 500, true),
                new GitRepoMemberResponse("ohksj", "http://somethingProfileUrl", 101, 1001, 501, true));
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
