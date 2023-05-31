package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;

import java.util.List;

public interface GitOrganizationRepository {
    boolean existsByName(String name);

    List<GitOrganization> findByGithubId(String githubId);

//    List<GitOrganization> saveAll(Set<GitOrganization> gitOrganizations);
}
