package com.dragonguard.backend.gitrepo.repository;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {
    Optional<GitRepo> findByName(String name);

    boolean existsByName(String name);
}
