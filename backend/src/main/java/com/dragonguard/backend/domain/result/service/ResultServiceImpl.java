package com.dragonguard.backend.domain.result.service;

import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.result.mapper.ResultMapper;
import com.dragonguard.backend.domain.result.repository.ResultRepository;
import com.dragonguard.backend.domain.search.dto.client.GitRepoSearchClientResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.kafka.ScrapeResult;
import com.dragonguard.backend.domain.search.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.search.dto.response.UserResultSearchResponse;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author 김승진
 * @description 검색 결과에 대한 서비스 로직을 수행하는 클래스
 */
@TransactionService
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;

    public void saveAllResultsWithSearch(
            final List<ScrapeResult> results, final Long searchId, final List<Result> resultList) {
        results.stream()
                .filter(
                        entity ->
                                resultRepository.existsByNameAndSearchId(
                                        entity.getFull_name(), searchId))
                .map(result -> resultMapper.toEntity(result.getFull_name(), searchId))
                .filter(r -> !resultList.contains(r))
                .forEach(resultRepository::save);
    }

    public List<Result> findAllBySearchId(final Long searchId) {
        return resultRepository.findAllBySearchId(searchId);
    }

    @Override
    public UserResultSearchResponse saveResult(
            final UserClientResponse response, final Search search, final boolean isServiceMember) {
        Result result = resultRepository.save(resultMapper.toEntity(response, search.getId()));
        return resultMapper.toUserResponse(result, isServiceMember);
    }

    @Override
    public GitRepoResultResponse saveResultAndGetGitRepoResponse(
            final GitRepoSearchClientResponse response, final Search search) {
        Result result =
                resultRepository.save(
                        resultMapper.toEntity(response.getFullName(), search.getId()));
        return resultMapper.toGitRepoResponse(result.getId(), response);
    }

    @Override
    public Result loadEntity(final Long id) {
        return resultRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
