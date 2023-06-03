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
    public GitRepo toEntity(GitRepoRequest gitRepoRequest, Boolean update) {
        return GitRepo.builder()
                .id(gitRepoRequest.getId())
                .update(update)
                .build();
    }

    public GitRepo toEntity(String id, Boolean update) {
        return GitRepo.builder()
                .id(id)
                .update(update)
                .build();
    }
}
