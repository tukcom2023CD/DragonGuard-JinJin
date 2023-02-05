package com.dragonguard.backend.gitrepo.controller;

import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.dragonguard.backend.gitrepomember.dto.GitRepoMemberResponse;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitRepoController.class)
class GitRepoControllerTest extends RestDocumentTest {

    @MockBean
    private GitRepoService gitRepoService;

    @Test
    void getRepoMembers() throws Exception {
        List<GitRepoMemberResponse> expected = List.of(
                new GitRepoMemberResponse("ohksj77", 100, 1000, 500),
                new GitRepoMemberResponse("HJ39", 101, 999, 500),
                new GitRepoMemberResponse("posite", 99, 1001, 500),
                new GitRepoMemberResponse("Sammuelwoojae", 100, 1001, 499));
        given(gitRepoService.findMembersByGitRepo(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/api/git-repos?name=tukcom2023CD/DragonGuard-JinJin")
                                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        perform.andDo(print())
                .andDo(document("get git-repo contributors", getDocumentRequest(), getDocumentResponse()));
    }
}
