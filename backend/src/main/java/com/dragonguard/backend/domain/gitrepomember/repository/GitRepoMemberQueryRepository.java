package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.Optional;

/**
 * @author 김승진
 * @description 레포지토리 내부 기여자들의 DB 접근을 처리하는 로직을 갖는 인터페이스
 */
public interface GitRepoMemberQueryRepository {
    Optional<GitRepoMember> findByGitRepoAndMember(final GitRepo gitRepo, final Member member);

    Optional<GitRepoMember> findByGitRepoAndMemberGithubId(
            final GitRepo gitRepo, final String githubId);
}
