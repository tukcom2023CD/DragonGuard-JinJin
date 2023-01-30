package com.dragonguard.backend.search.dto.request;

import com.dragonguard.backend.search.entity.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    @NotEmpty
    private String name;
    @NotNull
    private SearchType type;
    @NotNull
    private Integer page;
}
