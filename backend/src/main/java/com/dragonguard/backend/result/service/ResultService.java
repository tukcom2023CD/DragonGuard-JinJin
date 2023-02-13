package com.dragonguard.backend.result.service;

import com.dragonguard.backend.result.dto.request.ResultRequest;
import com.dragonguard.backend.result.entity.Result;
import com.dragonguard.backend.result.mapper.ResultMapper;
import com.dragonguard.backend.result.repository.ResultRepository;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.Search;
import com.dragonguard.backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final SearchService searchService;

    public void saveAllResult(List<ResultRequest> results, SearchRequest searchRequest) {
        Search search = searchService.getEntityByRequest(searchRequest);
        List<Result> resultList = resultRepository.findAllBySearchId(search.getId());

        results.stream()
                .map(result -> resultMapper.toEntity(result, search.getId()))
                .filter(r -> !resultList.contains(r))
                .forEach(resultRepository::save);
    }
}
