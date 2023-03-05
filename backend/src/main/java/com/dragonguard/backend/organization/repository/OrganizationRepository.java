package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB CRUD를 수행하는 클래스
 */

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
