package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.organization.entity.Organization;
import com.dragonguard.backend.organization.entity.OrganizationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB CRUD를 수행하는 클래스
 */

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, OrganizationQueryRepository {
    @Query(value = "SELECT o FROM Organization o WHERE o.organizationType = :organizationType")
    List<Organization> findByType(OrganizationType organizationType, Pageable pageable);

    Optional<Organization> findByNameAndOrganizationTypeAndEmailEndpoint(String name, OrganizationType organizationType, String emailEndpoint);
}
