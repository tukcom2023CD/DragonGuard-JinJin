package com.dragonguard.backend.domain.search.mapper;

import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.Filter;
import com.dragonguard.backend.domain.search.entity.Search;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 검색 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = ComponentModel.SPRING)
public interface SearchMapper {
    @Mapping(target = "filters", qualifiedByName = "getGitOrganizationNames")
    Search toSearch(final SearchRequest searchRequest);

    @Named("getGitOrganizationNames")
    default List<Filter> toFilterList(final List<String> filters) {
        if (Objects.isNull(filters) || filters.isEmpty()) return List.of();

        return filters.stream()
                .map(this::toFilter)
                .collect(Collectors.toList());
    }

    default Filter toFilter(final String filter) {
        return Filter.builder()
                .filter(filter)
                .build();
    }
}
