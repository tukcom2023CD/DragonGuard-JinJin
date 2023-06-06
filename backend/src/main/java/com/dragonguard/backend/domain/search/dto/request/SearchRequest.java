package com.dragonguard.backend.domain.search.dto.request;

import com.dragonguard.backend.domain.search.entity.SearchType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 김승진
 * @description 검색에 대한 요청 정보를 담는 dto
 */

@Getter
@ToString // Redis 때문에 붙임
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    @Setter
    private String githubToken;

    @NotBlank
    private String name;

    @NotNull
    private SearchType type;

    @NotNull
    private Integer page;

    private List<String> filters = new ArrayList<>();

    public SearchRequest(String name, SearchType type, Integer page) {
        this.name = name;
        this.type = type;
        this.page = page;
    }

    public SearchRequest(String name, SearchType type, Integer page, List<String> filters) {
        this.name = name;
        this.type = type;
        this.page = page;
        this.filters = filters;
    }
}
