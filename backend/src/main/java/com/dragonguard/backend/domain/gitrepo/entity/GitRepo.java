package com.dragonguard.backend.domain.gitrepo.entity;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.SoftDelete;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

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
public class GitRepo extends BaseTime implements Persistable<String> {

    @Id
    private String name;

    private Integer closedIssueNum;

    @OneToMany(mappedBy = "gitRepo", cascade = CascadeType.ALL)
    private Set<GitRepoMember> gitRepoMembers = new HashSet<>();

    @Transient
    private Boolean update;

    @Builder
    public GitRepo(String name, Set<GitRepoMember> gitRepoMembers, Boolean update) {
        this.name = name;
        this.gitRepoMembers = gitRepoMembers;
        this.update = update;
    }

    public void updateClosedIssueNum(Integer closedIssueNum) {
        this.closedIssueNum = closedIssueNum;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public boolean isNew() {
        return !this.update;
    }
}
