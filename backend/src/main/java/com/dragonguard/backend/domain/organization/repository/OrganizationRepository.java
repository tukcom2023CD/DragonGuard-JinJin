package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB CRUD를 수행하는 클래스
 */

public interface OrganizationRepository extends JpaRepository<Organization, Long>, OrganizationQueryRepository {
    @Query("SELECT o FROM Organization o WHERE o.organizationType = :organizationType AND o.organizationStatus = 'ACCEPTED'")
    List<Organization> findAllByType(final OrganizationType organizationType, final Pageable pageable);
    Optional<Organization> findByNameAndOrganizationTypeAndEmailEndpoint(final String name, final OrganizationType organizationType, final String emailEndpoint);
    @Query("SELECT o FROM Organization o WHERE o.name = :name AND o.organizationStatus = 'ACCEPTED'")
    Optional<Organization> findByName(final String name);
}
