package com.dragonguard.backend.domain.organization.repository;

import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository {
    List<Organization> findByType(OrganizationType organizationType, Pageable pageable);

    Optional<Organization> findByNameAndOrganizationTypeAndEmailEndpoint(String name, OrganizationType organizationType, String emailEndpoint);

    Optional<Organization> findByName(String name);

    Organization save(Organization organization);

    Optional<Organization> findById(Long id);
}
