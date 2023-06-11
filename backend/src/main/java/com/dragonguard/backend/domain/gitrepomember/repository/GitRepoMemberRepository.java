package com.dragonguard.backend.domain.gitrepomember.repository;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;

import java.util.List;

public interface GitRepoMemberRepository {
    <S extends GitRepoMember> List<S> saveAll(Iterable<S> entities);
    GitRepoMember save(GitRepoMember gitRepoMember);
}
