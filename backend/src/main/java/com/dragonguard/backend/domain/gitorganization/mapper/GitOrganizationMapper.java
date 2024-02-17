package com.dragonguard.backend.domain.gitorganization.mapper;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Member;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

/**
 * @author 김승진
 * @description 깃허브 조직 Entity와 dto의 변환을 돕는 클래스
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface GitOrganizationMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "member", source = "member")
    @Mapping(target = "profileImage", source = "profileImage")
    GitOrganization toEntity(final String name, final String profileImage, final Member member);
}
