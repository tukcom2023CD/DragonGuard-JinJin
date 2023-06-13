package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dragonguard.backend.domain.gitrepo.entity.QGitRepo.gitRepo;
import static com.dragonguard.backend.domain.gitrepomember.entity.QGitRepoMember.gitRepoMember;


/**
 * @author 김승진
 * @description 깃허브 Repository 기여자들의 정보들에 대한 DB 조회 접근에 대한 인터페이스의 구현체
 */

@Repository
@RequiredArgsConstructor
public class GitRepoMemberQueryRepositoryImpl implements GitRepoMemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GitRepoMember> findAllByGitRepo(GitRepo repo) {
        return jpaQueryFactory
                .selectFrom(gitRepoMember)
                .join(gitRepo.gitRepoMembers, gitRepoMember)
                .where()
                .distinct()
                .fetch();
    }

    @Override
    public boolean existsByGitRepoAndMember(GitRepo gitRepo, Member member) {
        return jpaQueryFactory
                .selectFrom(gitRepoMember)
                .where(equalGitRepoAndMember(gitRepo, member))
                .fetchFirst() != null;
    }

    @Override
    public Optional<GitRepoMember> findByGitRepoAndMember(GitRepo gitRepo, Member member) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(gitRepoMember)
                .where(equalGitRepoAndMember(gitRepo, member))
                .fetchFirst());
    }

    @Override
    public Optional<GitRepoMember> findByNameAndMemberName(String name, String githubId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(gitRepoMember)
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

    private BooleanExpression equalGitRepoAndMember(GitRepo gitRepo, Member member) {
        return gitRepoMember.gitRepo.eq(gitRepo)
                .and(gitRepoMember.member.eq(member));
    }
}
