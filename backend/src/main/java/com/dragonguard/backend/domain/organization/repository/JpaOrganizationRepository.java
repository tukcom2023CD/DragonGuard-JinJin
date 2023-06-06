package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB CRUD를 수행하는 클래스
 */

public interface JpaOrganizationRepository extends JpaRepository<Organization, Long>, OrganizationRepository {
    @Query(value = "SELECT o FROM Organization o WHERE o.organizationType = :organizationType")
    List<Organization> findAllByType(OrganizationType organizationType, Pageable pageable);
}
