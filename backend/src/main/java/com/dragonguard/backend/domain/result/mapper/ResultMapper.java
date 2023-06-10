package com.dragonguard.backend.domain.result.mapper;

import com.dragonguard.backend.domain.result.dto.client.ClientResultResponse;
import com.dragonguard.backend.domain.result.dto.response.ResultResponse;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.search.dto.client.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author 김승진
 * @description 검색 결과 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface ResultMapper {
    @Mapping(target = "name", source = "clientResultResponse.full_name")
    Result toEntity(final ClientResultResponse clientResultResponse, final Long searchId);
    @Mapping(target = "name", source = "userResponse.login")
    Result toEntity(final UserResponse userResponse, final Long searchId);
    ResultResponse toResponse(final Result result);
}
