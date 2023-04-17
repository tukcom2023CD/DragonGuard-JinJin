package com.dragonguard.backend.search.service;

import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.util.KafkaProducer;
import com.dragonguard.backend.util.GithubClient;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.result.entity.Result;
import com.dragonguard.backend.result.mapper.ResultMapper;
import com.dragonguard.backend.result.repository.ResultRepository;
import com.dragonguard.backend.search.dto.request.kafka.KafkaSearchRequest;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.dto.response.client.SearchRepoResponse;
import com.dragonguard.backend.search.dto.response.client.SearchUserResponse;
import com.dragonguard.backend.search.entity.Filter;
import com.dragonguard.backend.search.entity.Search;
import com.dragonguard.backend.search.entity.SearchType;
import com.dragonguard.backend.search.mapper.SearchMapper;
import com.dragonguard.backend.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author 김승진
 * @description 검색 관련 서비스 로직을 처리하는 클래스
 */

@Service
@Validated
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final ResultMapper resultMapper;
    private final ResultRepository resultRepository;
    private final AuthService authService;
    private final KafkaProducer<KafkaSearchRequest> kafkaSearchProducer;
    private final GithubClient<SearchRequest, SearchRepoResponse> githubRepoClient;
    private final GithubClient<SearchRequest, SearchUserResponse> githubUserClient;

    public List<ResultResponse> getSearchResult(SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        List<Result> results = resultRepository.findAllBySearchId(search.getId());
        if (results.isEmpty()) {
            requestScraping(searchRequest);
            return List.of();
        }
        return results.stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "results", key = "#searchRequest", cacheManager = "cacheManager")
    public List<ResultResponse> getSearchResultByClient(@Valid SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        List<Result> results = resultRepository.findAllBySearchId(search.getId());
        results.forEach(Result::delete);
        searchRequest.setGithubToken(authService.getLoginUser().getGithubToken());
        Object clientResult = requestClient(searchRequest);

        if (clientResult instanceof SearchRepoResponse) {
            return List.of(((SearchRepoResponse) clientResult).getItems()).stream()
                    .map(request -> resultRepository.save(resultMapper.toEntity(request, search.getId())))
                    .map(resultMapper::toResponse).collect(Collectors.toList());
        } else if (clientResult instanceof SearchUserResponse) {
            return List.of(((SearchUserResponse) clientResult).getItems()).stream()
                    .map(request -> resultRepository.save(resultMapper.toEntity(request, search.getId())))
                    .map(resultMapper::toResponse).collect(Collectors.toList());
        }
        return List.of();
    }

    public Search findOrSaveSearch(SearchRequest searchRequest) {
        if (searchRequest.getFilters() == null || searchRequest.getFilters().isEmpty()) {
            return searchRepository
                    .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage())
                    .stream().filter(entity -> entity.getFilters().isEmpty()).findFirst()
                    .orElseGet(() -> searchRepository.save(searchMapper.toEntity(searchRequest)));
        }
        List<Search> searches = searchRepository
                .findByNameAndTypeAndPage(searchRequest.getName(), searchRequest.getType(), searchRequest.getPage());

        List<String> filters = searchRequest.getFilters();
        for (Search search : searches) {
            if (search.getFilters().stream().map(Filter::getFilter).collect(Collectors.toList()).containsAll(filters)) {
                return search;
            }
        }

        return searchRepository.save(searchMapper.toEntity(searchRequest));
    }

    public Search getEntityByRequest(SearchRequest searchRequest) {
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
            if (search.getFilters().stream().map(Filter::getFilter).collect(Collectors.toList()).containsAll(filters)) {
                return search;
            }
        }

        throw new EntityNotFoundException();
    }

    private void requestScraping(SearchRequest searchRequest) {
        kafkaSearchProducer.send(
                new KafkaSearchRequest(
                        searchRequest.getName(),
                        searchRequest.getType().toString(),
                        searchRequest.getPage()));
    }

    private Object requestClient(SearchRequest searchRequest) {
        return getSearchComponent(searchRequest.getType()).apply(searchRequest);
    }

    private Function<SearchRequest, Object> getSearchComponent(SearchType searchType) {
        return searchType.equals(SearchType.REPOSITORIES) ? githubRepoClient::requestToGithub : githubUserClient::requestToGithub;
    }
}
