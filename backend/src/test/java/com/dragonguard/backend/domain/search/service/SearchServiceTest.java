package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.result.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Filter;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.domain.search.repository.SearchRepository;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DatabaseTest
@DisplayName("Search 서비스의")
class SearchServiceTest extends LoginTest {
    @Autowired private SearchService searchService;
    @Autowired private SearchRepository searchRepository;
    @MockBean private GithubClient<SearchRequest, SearchRepoResponse> githubRepoClient;
    @MockBean private GithubClient<SearchRequest, SearchUserResponse> githubUserClient;

    @Nested
    @DisplayName("검색을 통한 결과 요청 중")
    class SearchAndGetResult {
        @Test
        @DisplayName("레포 검색이 수행되는가")
        void getGitRepoSearchResultByClient() {
            //given
            String repoName = "tukcom2023CD/DragonGuard-JinJin";
            when(githubRepoClient.requestToGithub(any())).thenReturn(new SearchRepoResponse(new GitRepoClientResponse[]{new GitRepoClientResponse(repoName, "java", "good repo", LocalDateTime.now().toString())}));

            //when
//            List<GitRepoResultResponse> result = searchService.getGitRepoSearchResultByClient(repoName, 1, List.of("language:java"));

            //then
//            assertThat(result.get(0).getName()).isEqualTo(repoName);
        }

        @Test
        @DisplayName("유저 검색이 수행되는가")
        void getUsersSearchResultByClient() {
            //given
            String userName = "ohksj77";
            when(githubUserClient.requestToGithub(any())).thenReturn(new SearchUserResponse(new UserClientResponse[]{new UserClientResponse(userName)}));

            //when
//            List<UserResultResponse> result = searchService.getUserSearchResultByClient(userName, 1, List.of("language:java"));

            //then
//            assertThat(result.get(0).getName()).isEqualTo(userName);
        }
    }

    @Test
    @DisplayName("id를 통한 조회가 수행되는가")
    void loadEntity() {
        //given
        Search expected = searchRepository.save(Search.builder().name("ohksj77").type(SearchType.USERS).page(1).filters(List.of(Filter.builder().build())).build());

        //when
        Search result = searchService.loadEntity(expected.getId());

        //then
        assertThat(result).isEqualTo(expected);
    }
}
