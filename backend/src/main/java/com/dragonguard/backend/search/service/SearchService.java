package com.dragonguard.backend.search.service;

import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.webclient.GithubClient;
import com.dragonguard.backend.result.dto.request.ResultRequest;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.result.entity.Result;
import com.dragonguard.backend.result.mapper.ResultMapper;
import com.dragonguard.backend.result.repository.ResultRepository;
import com.dragonguard.backend.search.dto.request.KafkaSearchRequest;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.dto.response.SearchClientResponse;
import com.dragonguard.backend.search.entity.Search;
import com.dragonguard.backend.search.mapper.SearchMapper;
import com.dragonguard.backend.search.messagequeue.KafkaSearchProducer;
import com.dragonguard.backend.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final ResultMapper resultMapper;
    private final ResultRepository resultRepository;
    private final KafkaSearchProducer kafkaSearchProducer;
    private final GithubClient<SearchRequest, SearchClientResponse> githubClient;

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

    public List<ResultResponse> getSearchResultByClient(SearchRequest searchRequest) {
        Search search = findOrSaveSearch(searchRequest);
        List<Result> results = resultRepository.findAllBySearchId(search.getId());
        if (results.isEmpty()) {
            SearchClientResponse searchClientResponse = requestClient(searchRequest);

            return Arrays.asList(searchClientResponse.getItems()).stream()
                    .map(request -> resultRepository.save(resultMapper.toEntity(request, search.getId())))
                    .map(resultMapper::toResponse).collect(Collectors.toList());
        }
        return results.stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Search findOrSaveSearch(SearchRequest searchRequest) {
        Search search = searchMapper.toEntity(searchRequest);
        ExampleMatcher exampleMatcher = matching()
                .withIgnorePaths("id")
                .withMatcher("name", exact())
                .withMatcher("page", exact())
                .withMatcher("type", exact().ignoreCase());

        return searchRepository
                .findOne(Example.of(search, exampleMatcher))
                .orElseGet(() -> searchRepository.save(searchMapper.toEntity(searchRequest)));
    }

    public Search getEntityByRequest(SearchRequest searchRequest) {
        Search search = searchMapper.toEntity(searchRequest);
        ExampleMatcher exampleMatcher = matching()
                .withIgnorePaths("id")
                .withMatcher("name", exact())
                .withMatcher("page", exact())
                .withMatcher("type", exact().ignoreCase());

        return searchRepository
                .findOne(Example.of(search, exampleMatcher))
                .orElseThrow(EntityNotFoundException::new);
    }

    private void requestScraping(SearchRequest searchRequest) {
        kafkaSearchProducer.send(
                new KafkaSearchRequest(
                        searchRequest.getName(),
                        searchRequest.getType().toString(),
                        searchRequest.getPage()));
    }

    private SearchClientResponse requestClient(SearchRequest searchRequest) {
        return githubClient.requestToGithub(searchRequest);
    }
}
