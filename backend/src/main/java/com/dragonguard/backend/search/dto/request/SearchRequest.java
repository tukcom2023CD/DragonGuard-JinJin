package com.dragonguard.backend.search.dto.request;

import com.dragonguard.backend.search.entity.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 김승진
 * @description 검색에 대한 요청 정보를 담는 dto
 */

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
