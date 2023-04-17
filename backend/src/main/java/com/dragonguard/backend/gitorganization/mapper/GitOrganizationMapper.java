package com.dragonguard.backend.gitorganization.mapper;

import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Organization을 만들어주는 Mappper
 */

@Component
public class GitOrganizationMapper {
    public GitOrganization toEntity(String name, Member member) {
        return GitOrganization.builder()
                .name(name)
                .member(member)
                .build();
    }
}
