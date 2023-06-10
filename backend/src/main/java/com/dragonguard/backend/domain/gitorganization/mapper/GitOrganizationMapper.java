package com.dragonguard.backend.domain.gitorganization.mapper;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Member;
import org.mapstruct.Mapper;

/**
 * @author 김승진
 * @description 깃허브 조직 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface GitOrganizationMapper {
    GitOrganization toEntity(final String name, final Member member);
}
