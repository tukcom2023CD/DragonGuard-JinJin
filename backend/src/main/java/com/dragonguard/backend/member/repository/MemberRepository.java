package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 김승진
 * @description 멤버 DB CRUD를 담당하는 인터페이스
 */

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
    Optional<Member> findMemberByGithubId(String githubId);
    Optional<Member> findGithubIdById(Long id);
    boolean existsByGithubId(String githubId);
}
