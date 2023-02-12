package com.dragonguard.backend.search.dto.request;

import com.dragonguard.backend.search.entity.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    @NotBlank
    private String name;
    @NotBlank
    private SearchType type;
    @NotNull
    private Integer page;
}
