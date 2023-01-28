package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByGithubId(String githubId);

    String findGithubIdById(Long id);

    @Query(value = "UPDATE Member m SET m.tier = :tier WHERE m.id = :id")
    void updateTierById(Long id, Tier tier);
}
