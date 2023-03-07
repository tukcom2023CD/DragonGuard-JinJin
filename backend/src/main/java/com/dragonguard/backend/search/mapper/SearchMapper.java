package com.dragonguard.backend.search.mapper;

import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.Filter;
import com.dragonguard.backend.search.entity.Search;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 검색 Entity와 dto 사이의 변환을 돕는 클래스
 */

@Component
public class SearchMapper {
    public Search toEntity(SearchRequest searchRequest) {
        List<String> filters = searchRequest.getFilters();
        return Search.builder()
                .name(searchRequest.getName())
                .type(searchRequest.getType())
                .page(searchRequest.getPage())
                .filters(filters == null || filters.isEmpty() ? List.of() : toFilterList(filters))
                .build();
    }

    private List<Filter> toFilterList(List<String> filters) {
        return filters.stream()
                .map(this::toFilterEntity)
                .collect(Collectors.toList());
    }

    private Filter toFilterEntity(String filter) {
        return Filter.builder()
                .filter(filter)
                .build();
    }
}
