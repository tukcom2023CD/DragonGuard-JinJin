package com.dragonguard.backend.result.mapper;

import com.dragonguard.backend.result.dto.request.ResultRequest;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.result.entity.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultMapper {

    public Result toEntity(ResultRequest resultRequest, String searchId) {
        return Result.builder()
                .name(resultRequest.getFull_name())
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
