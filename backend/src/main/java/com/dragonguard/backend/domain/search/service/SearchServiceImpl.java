package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Filter;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.domain.search.mapper.SearchMapper;
import com.dragonguard.backend.domain.search.repository.SearchRepository;
import com.dragonguard.backend.global.client.GithubClient;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description 검색 관련 서비스 로직을 처리하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class SearchServiceImpl implements EntityLoader<Search, Long>, SearchService {
    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final GithubClient<SearchRequest, SearchRepoResponse> githubRepoClient;
    private final GithubClient<SearchRequest, SearchUserResponse> githubUserClient;

    @Override
    public Search findOrSaveSearch(final SearchRequest searchRequest) {
        if (isValidSearchRequest(searchRequest)) {
            return findOrGetSearchWithSearchAttributes(searchRequest);
        }
        List<Search> searches = searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage());
        List<String> filters = searchRequest.getFilters();

        return Optional.ofNullable(getSameSearch(searches, filters)).orElseGet(() ->  searchRepository.save(searchMapper.toSearch(searchRequest)));
    }

    private Search getSameSearch(final List<Search> searches, final List<String> filters) {
        return searches.stream().filter(search -> containsSameFilters(filters, search)).findFirst().orElse(null);
    }

    private boolean containsSameFilters(final List<String> filters, final Search search) {
        return new HashSet<>(search.getFilters().stream().map(Filter::getFilter).collect(Collectors.toList())).containsAll(filters);
    }

    private Search findOrGetSearchWithSearchAttributes(final SearchRequest searchRequest) {
        return searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage())
                .stream().filter(entity -> entity.getFilters().isEmpty()).findFirst()
                .orElseGet(() -> searchRepository.save(searchMapper.toSearch(searchRequest)));
    }

    private boolean isValidSearchRequest(final SearchRequest searchRequest) {
        return Objects.isNull(searchRequest.getFilters()) || searchRequest.getFilters().isEmpty();
    }

    @Override
    public Search loadEntity(final Long id) {
        return searchRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Cacheable(value = "userResults", key = "{#searchRequest}", cacheManager = "cacheManager")
    public SearchUserResponse requestUserToGithub(final SearchRequest searchRequest) {
        return githubUserClient.requestToGithub(searchRequest);
    }

    @Cacheable(value = "repoResults", key = "{#searchRequest}", cacheManager = "cacheManager")
    public SearchRepoResponse requestRepoToGithub(final SearchRequest searchRequest) {
        return githubRepoClient.requestToGithub(searchRequest);
    }
}
