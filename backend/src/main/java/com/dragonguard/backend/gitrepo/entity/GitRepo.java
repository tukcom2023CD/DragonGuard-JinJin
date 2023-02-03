package com.dragonguard.backend.gitrepo.entity;

import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepo extends BaseTime {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GitRepoMember gitRepoMember;

}
