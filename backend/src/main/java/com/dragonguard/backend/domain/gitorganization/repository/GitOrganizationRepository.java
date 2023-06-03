package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;

import java.util.List;

public interface GitOrganizationRepository {
    boolean existsById(String id);

    List<GitOrganization> findByGithubId(String githubId);

    <S extends GitOrganization> List<S> saveAll(Iterable<S> entities);
}
