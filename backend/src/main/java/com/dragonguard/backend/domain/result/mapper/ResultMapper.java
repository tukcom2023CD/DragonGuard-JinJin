package com.dragonguard.backend.domain.result.mapper;

import com.dragonguard.backend.domain.result.entity.Result;
import com.dragonguard.backend.domain.search.dto.client.GitRepoSearchClientResponse;
import com.dragonguard.backend.domain.search.dto.client.UserClientResponse;
import com.dragonguard.backend.domain.search.dto.response.GitRepoResultResponse;
import com.dragonguard.backend.domain.search.dto.response.UserResultSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

/**
 * @author 김승진
 * @description 검색 결과 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = ComponentModel.SPRING)
public interface ResultMapper {
    Result toEntity(final String name, final Long searchId);

    @Mapping(target = "name", source = "userResponse.login")
    Result toEntity(final UserClientResponse userResponse, final Long searchId);

    UserResultSearchResponse toUserResponse(final Result result, final Boolean isServiceMember);

    @Mapping(target = "name", source = "dto.fullName")
    @Mapping(target = "language", source = "dto.language")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "createdAt", source = "dto.createdAt")
    @Mapping(target = "id", source = "searchId")
    GitRepoResultResponse toGitRepoResponse(final Long searchId, final GitRepoSearchClientResponse dto);
}
