package com.dragonguard.backend.domain.result.mapper;

import com.dragonguard.backend.domain.result.dto.response.ResultResponse;
import com.dragonguard.backend.domain.result.dto.response.client.ClientResultResponse;
import com.dragonguard.backend.domain.search.dto.response.client.UserResponse;
import com.dragonguard.backend.domain.result.entity.Result;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 검색 결과 Entity와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class ResultMapper {

    public Result toEntity(final ClientResultResponse clientResultResponse, final Long searchId) {
        return Result.builder()
                .name(clientResultResponse.getFull_name())
                .searchId(searchId)
                .build();
    }

    public Result toEntity(final UserResponse userResponse, final Long searchId) {
        return Result.builder()
                .name(userResponse.getLogin())
                .searchId(searchId)
                .build();
    }

    public ResultResponse toResponse(final Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .build();
    }
}
