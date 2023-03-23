package com.dragonguard.backend.gitrepo.entity;

import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.BaseTime;
import com.dragonguard.backend.global.SoftDelete;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepo extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer closedIssues; // TODO 추후 추가할 Statistics 도메인으로 이동 필요

    @OneToMany(mappedBy = "gitRepo")
    private Set<GitRepoMember> gitRepoMembers = new HashSet<>();

    @Builder
    public GitRepo(String name, Set<GitRepoMember> gitRepoMembers) {
        this.name = name;
        this.gitRepoMembers = gitRepoMembers;
    }

    public void updateClosedIssues(Integer closedIssues) {
        this.closedIssues = closedIssues;
    }
}
