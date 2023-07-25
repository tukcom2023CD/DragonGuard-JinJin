package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.result.service.ResultService;
import com.dragonguard.backend.domain.result.service.ResultServiceImpl;
import com.dragonguard.backend.domain.search.dto.client.GitRepoSearchClientResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.kafka.ScrapeResult;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.search.dto.response.UserResultSearchResponse;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

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
    private final MemberService memberService;
    private final AuthService authService;

    @Override
    public Search findOrSaveSearch(final SearchRequest searchRequest) {
        return searchServiceImpl.findOrSaveSearch(searchRequest);
    }

    @Override
    public UserResultSearchResponse saveResult(final UserClientResponse response, final Search search, final boolean isServiceMember) {
        return resultServiceImpl.saveResult(response, search, isServiceMember);
    }

    @Override
    public GitRepoResultResponse saveResultAndGetGitRepoResponse(final GitRepoSearchClientResponse response, final Search search) {
        return resultServiceImpl.saveResultAndGetGitRepoResponse(response, search);
    }

    public void saveAllResult(final List<ScrapeResult> results, final SearchRequest searchRequest) {
        Long searchId = findOrSaveSearch(searchRequest).getId();
        List<Result> resultList = resultServiceImpl.findAllBySearchId(searchId);

        resultServiceImpl.saveAllResultsWithSearch(results, searchId, resultList);
    }

    public List<UserResultSearchResponse> getUserSearchResultByClient(final String name, final Integer page) {
        SearchRequest searchRequest = new SearchRequest(name, SearchType.USERS, page);
        Search search = getSearch(searchRequest);
        return searchUser(searchRequest, search);
    }

    public List<GitRepoResultResponse> getGitRepoSearchResultByClient(final String name, final Integer page, final List<String> filters) {
        SearchRequest searchRequest = new SearchRequest(name, SearchType.REPOSITORIES, page, filters);
        Search search = getSearch(searchRequest);
        return searchRepo(searchRequest, search);
    }

    private Search getSearch(final SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        searchRequest.setGithubToken(authService.getLoginUser().getGithubToken());
        return search;
    }

    private List<UserResultSearchResponse> searchUser(final SearchRequest searchRequest, final Search search) {
        return Arrays.stream(searchServiceImpl.requestUserToGithub(searchRequest).getItems())
                .map(response -> saveResult(response, search, memberService.isServiceMember(response.getLogin())))
                .collect(Collectors.toList());
    }

    private List<GitRepoResultResponse> searchRepo(final SearchRequest searchRequest, final Search search) {
        return Arrays.stream(searchServiceImpl.requestRepoToGithub(searchRequest).getItems())
                .map(response -> saveResultAndGetGitRepoResponse(response, search))
                .collect(Collectors.toList());
    }
}
