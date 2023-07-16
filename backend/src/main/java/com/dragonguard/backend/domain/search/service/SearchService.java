package com.dragonguard.backend.domain.search.service;

import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Search;

public interface SearchService {
    Search findOrSaveSearch(final SearchRequest searchRequest);
}
