package com.dragonguard.backend.gitrepomember.entity;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.member.entity.Member;

import javax.persistence.*;

@Entity
public class GitRepoMember {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GitRepo gitRepo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
