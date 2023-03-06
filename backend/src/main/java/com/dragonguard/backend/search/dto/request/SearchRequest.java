package com.dragonguard.backend.search.dto.request;

import com.dragonguard.backend.search.entity.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    @NotBlank
    private String name;
    @NotNull
    private SearchType type;
    @NotNull
    private Integer page;
    private List<String> filters;

    public SearchRequest(String name, SearchType type, Integer page) {
        this.name = name;
        this.type = type;
        this.page = page;
    }
}
