package com.dragonguard.backend.gitrepomember.entity;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.global.BaseTime;
import com.dragonguard.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepoMember extends BaseTime {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gitRepoMember")
    private GitRepo gitRepo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Integer commits;

    private Integer additions;

    private Integer deletions;

    @Builder
    public GitRepoMember(GitRepo gitRepo, Member member, Integer commits, Integer additions, Integer deletions) {
        this.gitRepo = gitRepo;
        this.member = member;
        this.commits = commits;
        this.additions = additions;
        this.deletions = deletions;
    }
}
