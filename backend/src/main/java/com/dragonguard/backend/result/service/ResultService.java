package com.dragonguard.backend.result.service;

import com.dragonguard.backend.result.dto.response.client.ClientResultResponse;
import com.dragonguard.backend.result.entity.Result;
import com.dragonguard.backend.result.mapper.ResultMapper;
import com.dragonguard.backend.result.repository.ResultRepository;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 김승진
 * @description 검색 결과에 대한 서비스 로직을 수행하는 클래스
 */

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final SearchService searchService;

    public void saveAllResult(List<ClientResultResponse> results, SearchRequest searchRequest) {
        Long searchId = searchService.getEntityByRequest(searchRequest).getId();
        List<Result> resultList = resultRepository.findAllBySearchId(searchId);

        results.stream()
                .filter(entity -> resultRepository.existsByNameAndSearchId(entity.getFull_name(), searchId))
                .map(result -> resultMapper.toEntity(result, searchId))
                .filter(r -> !resultList.contains(r))
                .forEach(resultRepository::save);
    }
}
