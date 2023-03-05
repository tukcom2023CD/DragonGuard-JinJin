package com.dragonguard.backend.gitorganization.repository;

import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 깃허브 Organization 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface GitOrganizationRepository extends JpaRepository<GitOrganization, Long> {
}
