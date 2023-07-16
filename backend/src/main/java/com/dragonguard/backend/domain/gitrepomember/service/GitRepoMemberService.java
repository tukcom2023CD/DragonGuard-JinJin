package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;

import java.util.Set;

public interface GitRepoMemberService {
    void saveAllGitRepoMembers(Set<GitRepoMember> gitRepoMembers);
}
