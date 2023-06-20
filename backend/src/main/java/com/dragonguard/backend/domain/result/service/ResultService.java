package com.dragonguard.backend.domain.result.service;

import com.dragonguard.backend.domain.result.dto.kafka.ScrapeResult;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.result.mapper.ResultMapper;
import com.dragonguard.backend.domain.result.repository.ResultRepository;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.service.SearchService;
import com.dragonguard.backend.global.EntityLoader;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 김승진
 * @description 검색 결과에 대한 서비스 로직을 수행하는 클래스
 */

@Service
@RequiredArgsConstructor
public class ResultService implements EntityLoader<Result, Long> {
    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final SearchService searchService;

    public void saveAllResult(final List<ScrapeResult> results, final SearchRequest searchRequest) {
        Long searchId = searchService.findOrSaveSearch(searchRequest).getId();
        List<Result> resultList = resultRepository.findAllBySearchId(searchId);

        saveAllResultsWithSearch(results, searchId, resultList);
    }

    private void saveAllResultsWithSearch(List<ScrapeResult> results, Long searchId, List<Result> resultList) {
        results.stream()
                .filter(entity -> resultRepository.existsByNameAndSearchId(entity.getFull_name(), searchId))
                .map(result -> resultMapper.toEntity(result.getFull_name(), searchId))
                .filter(r -> !resultList.contains(r))
                .forEach(resultRepository::save);
    }

    @Override
    public Result loadEntity(final Long id) {
        return resultRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
