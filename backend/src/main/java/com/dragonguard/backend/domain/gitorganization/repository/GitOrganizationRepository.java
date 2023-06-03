package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;

import java.util.List;
import java.util.Optional;

public interface GitOrganizationRepository {
    List<GitOrganization> findByGithubId(String githubId);

    <S extends GitOrganization> List<S> saveAll(Iterable<S> entities);

    Optional<GitOrganization> findByName(String name);
}
