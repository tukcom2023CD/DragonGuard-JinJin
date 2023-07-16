package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.result.service.ResultService;
import com.dragonguard.backend.domain.result.service.ResultServiceImpl;
import com.dragonguard.backend.domain.search.dto.client.GitRepoSearchClientResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.kafka.ScrapeResult;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.search.dto.response.UserResultSearchResponse;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 파사드 패턴으로 뽑아낸 검색 파사드 서비스
 */

@TransactionService
@RequiredArgsConstructor
public class SearchResultFacade implements SearchService, ResultService {
    private final SearchServiceImpl searchServiceImpl;
    private final ResultServiceImpl resultServiceImpl;
    private final AuthService authService;

    @Override
    public Search findOrSaveSearch(final SearchRequest searchRequest) {
        return searchServiceImpl.findOrSaveSearch(searchRequest);
    }

    @Override
    public void deleteAllLastResults(final Search search) {
        resultServiceImpl.deleteAllLastResults(search);
    }

    @Override
    public UserResultSearchResponse saveResult(final UserClientResponse response, final Search search) {
        return resultServiceImpl.saveResult(response, search);
    }

    @Override
    public GitRepoResultResponse saveAndGetGitRepoResponse(final GitRepoSearchClientResponse response, final Search search) {
        return resultServiceImpl.saveAndGetGitRepoResponse(response, search);
    }

    public void saveAllResult(final List<ScrapeResult> results, final SearchRequest searchRequest) {
        Long searchId = findOrSaveSearch(searchRequest).getId();
        List<Result> resultList = resultServiceImpl.findAllBySearchId(searchId);

        resultServiceImpl.saveAllResultsWithSearch(results, searchId, resultList);
    }

    @Cacheable(value = "userResults", key = "{#name, #page}", cacheManager = "cacheManager")
    public List<UserResultSearchResponse> getUserSearchResultByClient(final String name, final Integer page) {
        SearchRequest searchRequest = new SearchRequest(name, SearchType.USERS, page);
        Search search = getSearch(searchRequest);
        return searchUser(searchRequest, search);
    }

    @Cacheable(value = "repoResults", key = "{#name, #page, #filters}", cacheManager = "cacheManager")
    public List<GitRepoResultResponse> getGitRepoSearchResultByClient(final String name, final Integer page, final List<String> filters) {
        SearchRequest searchRequest = new SearchRequest(name, SearchType.REPOSITORIES, page, filters);
        Search search = getSearch(searchRequest);
        return searchRepo(searchRequest, search);
    }

    private Search getSearch(final SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        deleteAllLastResults(search);
        searchRequest.setGithubToken(authService.getLoginUser().getGithubToken());
        return search;
    }

    private List<UserResultSearchResponse> searchUser(final SearchRequest searchRequest, final Search search) {
        return Arrays.stream(searchServiceImpl.requestUserToGithub(searchRequest).getItems())
                .map(response -> saveResult(response, search))
                .collect(Collectors.toList());
    }

    private List<GitRepoResultResponse> searchRepo(final SearchRequest searchRequest, final Search search) {
        SearchRepoResponse clientResult = searchServiceImpl.requestRepoToGithub(searchRequest);
        return Arrays.stream(clientResult.getItems())
                .map(response -> saveAndGetGitRepoResponse(response, search))
                .collect(Collectors.toList());
    }
}
