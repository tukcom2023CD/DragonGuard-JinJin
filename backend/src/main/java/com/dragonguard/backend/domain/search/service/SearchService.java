package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.domain.result.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.result.dto.response.UserResultResponse;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.result.mapper.ResultMapper;
import com.dragonguard.backend.domain.result.repository.ResultRepository;
import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Filter;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.domain.search.mapper.SearchMapper;
import com.dragonguard.backend.domain.search.repository.SearchRepository;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description 검색 관련 서비스 로직을 처리하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class SearchService implements EntityLoader<Search, Long> {
    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final ResultMapper resultMapper;
    private final ResultRepository resultRepository;
    private final MemberService memberService;
    private final GithubClient<SearchRequest, SearchRepoResponse> githubRepoClient;
    private final GithubClient<SearchRequest, SearchUserResponse> githubUserClient;

    @Cacheable(value = "results", key = "{#name, #page, #filters}", cacheManager = "cacheManager")
    public List<UserResultResponse> getUserSearchResultByClient(String name, Integer page, List<String> filters) {
        SearchRequest searchRequest = new SearchRequest(name, SearchType.USERS, page, filters);
        Search search = getSearch(searchRequest);
        return searchUser(searchRequest, search);
    }

    @Cacheable(value = "results", key = "{#name, #page, #filters}", cacheManager = "cacheManager")
    public List<GitRepoResultResponse> getGitRepoSearchResultByClient(String name, Integer page, List<String> filters) {
        SearchRequest searchRequest = new SearchRequest(name, SearchType.REPOSITORIES, page, filters);
        Search search = getSearch(searchRequest);
        return searchRepo(searchRequest, search);
    }

    private Search getSearch(SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        deleteAllLastResults(search);
        searchRequest.setGithubToken(memberService.getLoginUserWithPersistence().getGithubToken());
        return search;
    }

    public void deleteAllLastResults(final Search search) {
        resultRepository.findAllBySearchId(search.getId()).forEach(Result::delete);
    }

    private List<UserResultResponse> searchUser(final SearchRequest searchRequest, final Search search) {
        return Arrays.stream(githubUserClient.requestToGithub(searchRequest).getItems())
                .map(request -> resultRepository.save(resultMapper.toEntity(request, search.getId())))
                .map(resultMapper::toUserResponse).collect(Collectors.toList());
    }

    private List<GitRepoResultResponse> searchRepo(final SearchRequest searchRequest, final Search search) {
        SearchRepoResponse clientResult = githubRepoClient.requestToGithub(searchRequest);
        return Arrays.stream(clientResult.getItems())
                .map(request -> {
                    Result result = resultRepository.save(resultMapper.toEntity(request.getFull_name(), search.getId()));
                    return resultMapper.toGitRepoResponse(result.getId(), request);
                }).collect(Collectors.toList());
    }

    public Search findOrSaveSearch(final SearchRequest searchRequest) {
        if (isValidSearchRequest(searchRequest)) {
            return findOrGetSearchWithSearchAttributes(searchRequest);
        }
        List<Search> searches = searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage());
        List<String> filters = searchRequest.getFilters();

        return Optional.ofNullable(getSameSearch(searches, filters)).orElseGet(() -> searchRepository.save(searchMapper.toSearch(searchRequest)));
    }

    private Search getSameSearch(List<Search> searches, List<String> filters) {
        return searches.stream().filter(search -> isContainsSameFilters(filters, search)).findFirst().orElse(null);
    }

    private boolean isContainsSameFilters(List<String> filters, Search search) {
        return new HashSet<>(search.getFilters().stream().map(Filter::getFilter).collect(Collectors.toList())).containsAll(filters);
    }

    private Search findOrGetSearchWithSearchAttributes(SearchRequest searchRequest) {
        return searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage())
                .stream().filter(entity -> entity.getFilters().isEmpty()).findFirst()
                .orElseGet(() -> searchRepository.save(searchMapper.toSearch(searchRequest)));
    }

    private boolean isValidSearchRequest(SearchRequest searchRequest) {
        return searchRequest.getFilters() == null || searchRequest.getFilters().isEmpty();
    }

    @Override
    public Search loadEntity(final Long id) {
        return searchRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
