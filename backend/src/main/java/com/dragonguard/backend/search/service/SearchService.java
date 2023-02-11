package com.dragonguard.backend.search.service;

import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.result.entity.Result;
import com.dragonguard.backend.result.mapper.ResultMapper;
import com.dragonguard.backend.result.repository.ResultRepository;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.Search;
import com.dragonguard.backend.search.mapper.SearchMapper;
import com.dragonguard.backend.search.messagequeue.KafkaSearchProducer;
import com.dragonguard.backend.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final ResultMapper resultMapper;
    private final ResultRepository resultRepository;
    private final KafkaSearchProducer kafkaSearchProducer;

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

    public Search findOrSaveSearch(SearchRequest searchRequest) {
        Optional<Search> search = searchRepository
                .findBySearchWordAndPageAndSearchType(searchRequest.getName(), searchRequest.getPage(), searchRequest.getType());
        return search.orElseGet(() -> searchRepository.save(searchMapper.toEntity(searchRequest)));
    }

    public Search getEntityByRequest(SearchRequest searchRequest) {
        return searchRepository
                .findBySearchWordAndPageAndSearchType(searchRequest.getName(), searchRequest.getPage(), searchRequest.getType())
                .orElseThrow(EntityNotFoundException::new);
    }

    private void requestScraping(SearchRequest searchRequest) {
        kafkaSearchProducer.send(searchRequest);
    }
}
