package com.dragonguard.backend.gitorganization.mapper;

import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Organization을 만들어주는 Mappper
 */

@Component
public class GitOrganizationMapper {
    public GitOrganization toEntity(String name) {
        return GitOrganization.builder()
                .name(name)
                .build();
    }
}
