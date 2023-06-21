package com.dragonguard.backend.domain.search.dto.client;

import com.dragonguard.backend.domain.result.dto.client.GitRepoClientResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description Repository 검색에 대한 Github REST API 응답 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRepoResponse {
    private GitRepoClientResponse[] items;
}
