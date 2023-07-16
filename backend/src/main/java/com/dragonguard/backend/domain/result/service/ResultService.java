package com.dragonguard.backend.domain.result.service;

import com.dragonguard.backend.domain.search.dto.client.GitRepoSearchClientResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.search.dto.response.UserResultSearchResponse;
import com.dragonguard.backend.domain.search.entity.Search;

public interface ResultService {
    void deleteAllLastResults(final Search search);
    UserResultSearchResponse saveResult(final UserClientResponse response, final Search search);
    GitRepoResultResponse saveAndGetGitRepoResponse(final GitRepoSearchClientResponse request, final Search search);
}
