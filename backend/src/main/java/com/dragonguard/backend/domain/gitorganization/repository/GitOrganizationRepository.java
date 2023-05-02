package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Organization 관련 DB 접근을 도와주는 Repository
 */

@Repository
public interface GitOrganizationRepository extends JpaRepository<GitOrganization, Long> {
    boolean existsByName(String name);

    @Query("SELECT DISTINCT go FROM GitOrganization go JOIN FETCH go.gitOrganizationMembers gom JOIN FETCH gom.member m WHERE m.githubId = :githubId")
    List<GitOrganization> findByGithubId(String githubId);
}
