package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.domain.result.dto.response.ResultResponse;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.result.mapper.ResultMapper;
import com.dragonguard.backend.domain.result.repository.ResultRepository;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
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
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author 김승진
 * @description 검색 관련 서비스 로직을 처리하는 클래스
 */

@Validated
@TransactionService
@RequiredArgsConstructor
public class SearchService implements EntityLoader<Search, Long> {
    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final ResultMapper resultMapper;
    private final ResultRepository resultRepository;
    private final AuthService authService;
    private final GithubClient<SearchRequest, SearchRepoResponse> githubRepoClient;
    private final GithubClient<SearchRequest, SearchUserResponse> githubUserClient;

    @Cacheable(value = "results", key = "#searchRequest", cacheManager = "cacheManager")
    public List<ResultResponse> getSearchResultByClient(@Valid final SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        resultRepository.findAllBySearchId(search.getId()).forEach(Result::delete);
        searchRequest.setGithubToken(authService.getLoginUser().getGithubToken());

        if (searchRequest.getType().equals(SearchType.REPOSITORIES)) {
            return searchRepo(searchRequest, search);
        }
        return searchUser(searchRequest, search);
    }

    private List<ResultResponse> searchUser(final SearchRequest searchRequest, final Search search) {
        SearchUserResponse clientResult = githubUserClient.requestToGithub(searchRequest);
        return Stream.of(clientResult.getItems())
                .map(request -> resultRepository.save(resultMapper.toEntity(request, search.getId())))
                .map(resultMapper::toResponse).collect(Collectors.toList());
    }

    private List<ResultResponse> searchRepo(final SearchRequest searchRequest, final Search search) {
        SearchRepoResponse clientResult = githubRepoClient.requestToGithub(searchRequest);
        return Stream.of(clientResult.getItems())
                .map(request -> resultRepository.save(resultMapper.toEntity(request, search.getId())))
                .map(resultMapper::toResponse).collect(Collectors.toList());
    }

    public Search findOrSaveSearch(final SearchRequest searchRequest) {
        if (searchRequest.getFilters() == null || searchRequest.getFilters().isEmpty()) {
            return searchRepository
                    .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage())
                    .stream().filter(entity -> entity.getFilters().isEmpty()).findFirst()
                    .orElseGet(() -> searchRepository.save(searchMapper.toSearch(searchRequest)));
        }
        List<Search> searches = searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage());

        List<String> filters = searchRequest.getFilters();
        for (Search search : searches) {
            if (new HashSet<>(search.getFilters().stream().map(Filter::getFilter).collect(Collectors.toList())).containsAll(filters)) {
                return search;
            }
        }

        return searchRepository.save(searchMapper.toSearch(searchRequest));
    }

    public Search getEntityByRequest(final SearchRequest searchRequest) {
        if (searchRequest.getFilters() == null || searchRequest.getFilters().isEmpty()) {
            return searchRepository
                    .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage())
                    .stream().filter(entity -> entity.getFilters().isEmpty()).findFirst()
                    .orElseThrow(EntityNotFoundException::new);
        }

        List<Search> searches = searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage());

        List<String> filters = searchRequest.getFilters();
        for (Search search : searches) {
            if (new HashSet<>(search.getFilters().stream().map(Filter::getFilter).collect(Collectors.toList())).containsAll(filters)) {
                return search;
            }
        }

        throw new EntityNotFoundException();
    }

    @Override
    public Search loadEntity(final Long id) {
        return searchRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
