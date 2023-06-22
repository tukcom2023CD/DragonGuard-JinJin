package com.dragonguard.backend.domain.gitorganization.repository;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 조직관련 DB 접근을 수행하는 로직을 갖는 인터페이스
 */

public interface GitOrganizationRepository {
    List<GitOrganization> findAllByMember(Member member);
    <S extends GitOrganization> List<S> saveAll(Iterable<S> entities);
    Optional<GitOrganization> findByName(String name);
    Optional<GitOrganization> findById(Long id);
    boolean existsByName(String name);
}
