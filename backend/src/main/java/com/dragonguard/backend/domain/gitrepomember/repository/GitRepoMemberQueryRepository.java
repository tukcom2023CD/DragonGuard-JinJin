package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 Repository 기여자들의 정보들에 대한 DB 조회 접근에 대한 인터페이스
 */

public interface GitRepoMemberQueryRepository {
    Optional<GitRepoMember> findByGitRepoAndMember(GitRepo gitRepo, Member member);
    Optional<GitRepoMember> findByNameAndMemberGithubId(String name, String githubId);
    Optional<GitRepoMember> findById(Long id);
}
