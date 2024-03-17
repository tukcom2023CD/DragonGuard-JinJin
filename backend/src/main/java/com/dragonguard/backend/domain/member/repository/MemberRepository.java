package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

/**
 * @author 김승진
 * @description 멤버 DB CRUD를 담당하는 인터페이스
 */
public interface MemberRepository extends JpaRepository<Member, UUID>, MemberQueryRepository {
    Optional<Member> findByRefreshToken(final String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.refreshToken = :token WHERE m.id = :id")
    void updateRefreshToken(final UUID id, final String token);

    @Query(
            "SELECT m FROM Member m JOIN FETCH m.gitRepoMembers grm JOIN FETCH grm.gitRepo WHERE m.githubId = :githubId")
    Optional<Member> findByGithubIdWithGitRepoMember(final String githubId);
}
