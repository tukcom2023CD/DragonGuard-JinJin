package com.dragonguard.backend.search.entity;

import com.querydsl.core.types.dsl.ComparableExpressionBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.dragonguard.backend.gitrepo.entity.QGitRepo.gitRepo;

@Getter
@AllArgsConstructor
public enum GitRepoSortType {
    CREATED_AT(gitRepo.githubUpdatedAt);

    private final ComparableExpressionBase path;
}
