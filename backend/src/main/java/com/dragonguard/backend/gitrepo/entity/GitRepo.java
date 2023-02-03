package com.dragonguard.backend.gitrepo.entity;

import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepo extends BaseTime {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "gitRepo")
    private Set<GitRepoMember> gitRepoMember = new HashSet<>();

    @Builder
    public GitRepo(String name, Set<GitRepoMember> gitRepoMember) {
        this.name = name;
        this.gitRepoMember = gitRepoMember;
    }
}
