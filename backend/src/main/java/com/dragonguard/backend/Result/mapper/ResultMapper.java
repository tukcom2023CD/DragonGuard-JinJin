package com.dragonguard.backend.Result.mapper;

import com.dragonguard.backend.Result.dto.response.ResultResponse;
import com.dragonguard.backend.Result.entity.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultMapper {
    public ResultResponse toResponse(Result result) {
        return ResultResponse
                .builder()
                .id(result.getId())
                .name(result.getName())
                .build();
    }
}
