package com.dragonguard.backend.domain.gitrepo.mapper;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Repository Entity와 dto 사이의 변환을 돕는 클래스
 */

@Component
public class GitRepoMapper {
    public GitRepo toEntity(final GitRepoRequest gitRepoRequest) {
        return GitRepo.builder()
                .name(gitRepoRequest.getName())
                .build();
    }

    public GitRepo toEntity(final String name) {
        return GitRepo.builder()
                .name(name)
                .build();
    }
}
