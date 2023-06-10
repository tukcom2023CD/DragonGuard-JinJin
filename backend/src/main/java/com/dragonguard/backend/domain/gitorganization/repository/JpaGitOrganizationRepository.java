package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Organization 관련 DB 접근을 도와주는 Repository
 */

public interface JpaGitOrganizationRepository extends JpaRepository<GitOrganization, Long>, GitOrganizationRepository {

    @Query("SELECT DISTINCT go FROM GitOrganization go JOIN FETCH go.gitOrganizationMembers gom JOIN FETCH gom.member m WHERE m = :member")
    List<GitOrganization> findAllByMember(Member member);
}
