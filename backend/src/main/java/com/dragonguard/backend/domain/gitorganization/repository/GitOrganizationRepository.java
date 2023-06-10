package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface GitOrganizationRepository {
    List<GitOrganization> findAllByMember(Member member);
    <S extends GitOrganization> List<S> saveAll(Iterable<S> entities);
    Optional<GitOrganization> findByName(String name);
    Optional<GitOrganization> findById(Long id);
    boolean existsByName(String name);
}
