package com.dragonguard.backend.gitrepo.mapper;

import com.dragonguard.backend.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 깃허브 Repository Entity와 dto 사이의 변환을 돕는 클래스
 */

@Component
public class GitRepoMapper {
    public GitRepo toEntity(GitRepoRequest gitRepoRequest) {
        return GitRepo.builder()
                .name(gitRepoRequest.getName())
                .build();
    }
}
