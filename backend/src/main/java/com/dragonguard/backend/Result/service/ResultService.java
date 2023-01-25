package com.dragonguard.backend.Result.service;

import com.dragonguard.backend.Result.dto.response.ResultResponse;
import com.dragonguard.backend.Result.mapper.ResultMapper;
import com.dragonguard.backend.Result.repository.ResultQueryRepository;
import com.dragonguard.backend.search.entity.GitRepoSortType;
import com.dragonguard.backend.search.entity.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultMapper resultMapper;
    private final ResultQueryRepository resultQueryRepository;

    public List<ResultResponse> findAllRepositoryResult(
            String searchWord, GitRepoSortType gitRepoSortType,
            SortDirection sortDirection, Pageable pageable) {
        return resultQueryRepository.findAllRepositories(searchWord, gitRepoSortType, sortDirection, pageable).stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }
}
