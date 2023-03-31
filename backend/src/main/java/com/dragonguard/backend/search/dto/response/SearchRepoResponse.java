package com.dragonguard.backend.search.dto.response;

import com.dragonguard.backend.result.dto.response.ClientResultResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description Repository 검색에 대한 Github REST API 응답 정보를 담는 dto
 */

@Getter
@AllArgsConstructor
public class SearchRepoResponse {
    private ClientResultResponse[] items;
}
