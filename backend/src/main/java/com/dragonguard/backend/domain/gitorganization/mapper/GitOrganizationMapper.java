package com.dragonguard.backend.domain.gitorganization.mapper;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Organization을 만들어주는 Mappper
 */

@Component
public class GitOrganizationMapper {
    public GitOrganization toEntity(String id, Member member, Boolean update) {
        return GitOrganization.builder()
                .id(id)
                .member(member)
                .update(update)
                .build();
    }
}
