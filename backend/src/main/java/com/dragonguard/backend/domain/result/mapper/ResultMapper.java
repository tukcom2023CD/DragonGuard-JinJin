package com.dragonguard.backend.domain.result.mapper;

import com.dragonguard.backend.domain.result.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.result.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.result.dto.response.UserResultResponse;
import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author 김승진
 * @description 검색 결과 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface ResultMapper {
    Result toEntity(final String name, final Long searchId);

    @Mapping(target = "name", source = "userResponse.login")
    Result toEntity(final UserClientResponse userResponse, final Long searchId);

    UserResultResponse toUserResponse(final Result result);

    @Mapping(target = "name", source = "dto.full_name")
    @Mapping(target = "language", source = "dto.language")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "createdAt", source = "dto.created_at")
    @Mapping(target = "id", source = "searchId")
    GitRepoResultResponse toGitRepoResponse(final Long searchId, final GitRepoClientResponse dto);
}
