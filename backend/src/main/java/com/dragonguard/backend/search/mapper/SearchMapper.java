package com.dragonguard.backend.search.mapper;

import com.dragonguard.backend.Result.dto.response.ResultResponse;
import com.dragonguard.backend.search.dto.response.SearchResponse;
import com.dragonguard.backend.search.entity.Search;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchMapper {
    public SearchResponse toResponse(Search search, List<ResultResponse> resultResponses) {
        return SearchResponse
                .builder()
                .id(search.getId())
                .searchWord(search.getSearchWord())
                .resultResponses(resultResponses)
                .build();
    }
}
