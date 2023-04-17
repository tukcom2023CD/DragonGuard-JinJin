package com.dragonguard.backend.result.mapper;

import com.dragonguard.backend.result.dto.response.client.ClientResultResponse;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.result.entity.Result;
import com.dragonguard.backend.search.dto.response.client.UserResponse;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 검색 결과 Entity와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class ResultMapper {

    public Result toEntity(ClientResultResponse clientResultResponse, Long searchId) {
        return Result.builder()
                .name(clientResultResponse.getFull_name())
                .searchId(searchId)
                .build();
    }

    public Result toEntity(UserResponse userResponse, Long searchId) {
        return Result.builder()
                .name(userResponse.getLogin())
                .searchId(searchId)
                .build();
    }

    public ResultResponse toResponse(Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .build();
    }
}
