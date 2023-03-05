package com.dragonguard.backend.gitorganization.repository;

import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitOrganizationRepository extends JpaRepository<GitOrganization, Long> {
}
