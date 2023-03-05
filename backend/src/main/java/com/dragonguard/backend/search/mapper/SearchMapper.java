package com.dragonguard.backend.search.mapper;

import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.Search;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 검색 Entity와 dto 사이의 변환을 돕는 클래스
 */

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
