package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dragonguard.backend.domain.gitrepomember.entity.QGitRepoMember.gitRepoMember;
import static com.dragonguard.backend.domain.member.entity.QMember.member;


/**
 * @author 김승진
 * @description 깃허브 Repository 기여자들의 정보들에 대한 DB 조회 접근에 대한 인터페이스의 구현체
 */

@Repository
@RequiredArgsConstructor
public class GitRepoMemberQueryRepositoryImpl implements GitRepoMemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<GitRepoMember> findByGitRepoAndMember(GitRepo gitRepo, Member member) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(gitRepoMember)
                .where(gitRepoMember.gitRepo.eq(gitRepo)
                        .and(gitRepoMember.member.eq(member)))
                .fetchFirst());
    }

    @Override
    public Optional<GitRepoMember> findByNameAndMemberGithubId(String name, String githubId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(gitRepoMember)
                .leftJoin(gitRepoMember.member, member)
                .fetchJoin()
                .where(gitRepoMember.gitRepo.name.eq(name).and(gitRepoMember.member.githubId.eq(githubId)))
                .fetchFirst());
    }

    @Override
    public Optional<GitRepoMember> findById(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(gitRepoMember)
                .where(gitRepoMember.id.eq(id))
                .fetchFirst());
    }
}
