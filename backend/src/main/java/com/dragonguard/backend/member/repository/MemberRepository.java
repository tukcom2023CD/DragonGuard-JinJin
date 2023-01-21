package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
