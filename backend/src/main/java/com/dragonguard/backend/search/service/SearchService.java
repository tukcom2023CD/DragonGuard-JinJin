package com.dragonguard.backend.search.service;

import com.dragonguard.backend.Result.dto.response.ResultResponse;
import com.dragonguard.backend.Result.service.ResultService;
import com.dragonguard.backend.search.entity.GitRepoSortType;
import com.dragonguard.backend.search.entity.SortDirection;
import com.dragonguard.backend.search.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;
    private final ResultService resultService;

    public List<ResultResponse> getAllRepositoryName(String searchWord, GitRepoSortType gitRepoSortType, SortDirection sortDirection, Pageable pageable) {
        return resultService.findAllRepositoryResult(searchWord, gitRepoSortType, sortDirection, pageable);
    }
}
