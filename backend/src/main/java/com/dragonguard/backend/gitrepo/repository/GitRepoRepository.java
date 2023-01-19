package com.dragonguard.backend.gitrepo.repository;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {
}
