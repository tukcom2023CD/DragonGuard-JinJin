package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
    Optional<Member> findMemberByGithubId(String githubId);
    String findGithubIdById(Long id);
    boolean existsByGithubId(String githubId);
}
