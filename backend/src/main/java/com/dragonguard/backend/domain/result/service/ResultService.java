package com.dragonguard.backend.domain.result.service;

import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.search.dto.client.GitRepoSearchClientResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.kafka.ScrapeResult;
import com.dragonguard.backend.domain.search.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.search.dto.response.UserResultSearchResponse;
import com.dragonguard.backend.domain.search.entity.Search;

import java.util.List;

public interface ResultService {
    UserResultSearchResponse saveResult(final UserClientResponse response, final Search search, final boolean isServiceMember);
    GitRepoResultResponse saveResultAndGetGitRepoResponse(final GitRepoSearchClientResponse request, final Search search);
    List<Result> findAllBySearchId(Long searchId);
    void saveAllResultsWithSearch(List<ScrapeResult> results, Long searchId, List<Result> resultList);
}
