package com.dragonguard.backend.domain.search.dto.request;

import com.dragonguard.backend.domain.search.entity.SearchType;

import lombok.*;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 검색에 대한 요청 정보를 담는 dto
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "type", "page", "filters"})
public class SearchRequest {
    @Setter private String githubToken;

    @NotBlank private String name;

    @NotNull private SearchType type;

    @NotNull private Integer page;

    private List<String> filters;

    public SearchRequest(final String name, final SearchType type, final Integer page) {
        this.name = name;
        this.type = type;
        this.page = page;
    }

    public SearchRequest(
            final String name,
            final SearchType type,
            final Integer page,
            final List<String> filters) {
        this.name = name;
        this.type = type;
        this.page = page;
        this.filters = filters;
    }
}
