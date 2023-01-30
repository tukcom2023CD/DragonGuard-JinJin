package com.dragonguard.backend.search.mapper;

import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.Search;
import org.springframework.stereotype.Component;

@Component
public class SearchMapper {
    public Search toEntity(SearchRequest searchRequest) {
        return Search.builder()
                .searchWord(searchRequest.getName())
                .searchType(searchRequest.getType())
                .page(searchRequest.getPage())
                .build();
    }
}
