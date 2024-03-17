package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.search.dto.client.SearchRepoResponse;
import com.dragonguard.backend.domain.search.dto.client.SearchUserResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Search;
import com.dragonguard.backend.global.template.service.EntityLoader;

public interface SearchService extends EntityLoader<Search, Long> {
    Search findOrSaveSearch(final SearchRequest searchRequest);

    SearchUserResponse requestUserToGithub(final SearchRequest searchRequest);

    SearchRepoResponse requestRepoToGithub(final SearchRequest searchRequest);
}
