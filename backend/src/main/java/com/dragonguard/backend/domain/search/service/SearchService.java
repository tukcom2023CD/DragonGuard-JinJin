package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Search;

public interface SearchService {
    Search findOrSaveSearch(final SearchRequest searchRequest);
    SearchUserResponse requestUserToGithub(SearchRequest searchRequest);
    SearchRepoResponse requestRepoToGithub(SearchRequest searchRequest);
}
