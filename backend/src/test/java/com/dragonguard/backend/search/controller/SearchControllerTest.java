package com.dragonguard.backend.search.controller;

import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.search.service.SearchService;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
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


@WebMvcTest(SearchController.class)
class SearchControllerTest extends RestDocumentTest {

    @MockBean private SearchService searchService;

    @Test
    @DisplayName("검색 결과 조회")
    void getsearchresult() throws Exception {
        // given
        List<ResultResponse> expected = Arrays.asList(
                new ResultResponse("1234", "ohksj77"),
                new ResultResponse("5678", "HJ39"),
                new ResultResponse("8765", "posite"),
                new ResultResponse("aaaa", "Sammuelwoojae"),
                new ResultResponse("bbbb", "And"),
                new ResultResponse("cccc", "DragonGuard-JinJin")

        );
        given(searchService.getSearchResult(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/api/search?page=1&name=gitrank&type=repositories")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // docs
        perform.andDo(print())
                .andDo(document("get search result", getDocumentRequest(), getDocumentResponse()));
    }
}
