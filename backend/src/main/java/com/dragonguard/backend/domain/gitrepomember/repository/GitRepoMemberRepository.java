package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 김승진
 * @description GitRepoMember 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface GitRepoMemberRepository extends JpaRepository<GitRepoMember, Long>, GitRepoMemberQueryRepository {
    <S extends GitRepoMember> List<S> saveAll(final Iterable<S> entities);
    GitRepoMember save(final GitRepoMember gitRepoMember);
    boolean existsByGitRepoAndMember(final GitRepo gitRepo, final Member member);
}
