package com.dragonguard.backend.domain.gitrepo.entity;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 담는 DB Entity
 */

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepo implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Integer closedIssueNum;

    @OneToMany(mappedBy = "gitRepo", cascade = CascadeType.PERSIST)
    private Set<GitRepoMember> gitRepoMembers = new HashSet<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitRepo(String name, Set<GitRepoMember> gitRepoMembers) {
        this.name = name;
        this.gitRepoMembers = gitRepoMembers;
    }

    public void updateClosedIssueNum(Integer closedIssueNum) {
        this.closedIssueNum = closedIssueNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitRepo gitRepo = (GitRepo) o;
        return Objects.equals(name, gitRepo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
