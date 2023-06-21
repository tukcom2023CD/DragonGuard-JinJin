package com.dragonguard.backend.domain.member.repository;

import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 DB CRUD를 담당하는 인터페이스
 */

public interface MemberRepository extends JpaRepository<Member, UUID>, MemberQueryRepository {

    @Query("SELECT m.refreshToken FROM Member m WHERE m.id = :id")
    String findRefreshTokenById(UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.refreshToken = :token WHERE m.id = :id")
    void updateRefreshToken(UUID id, String token);
}
