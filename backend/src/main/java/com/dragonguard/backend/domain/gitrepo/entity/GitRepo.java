package com.dragonguard.backend.domain.gitrepo.entity;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    @OneToMany(mappedBy = "gitRepo")
    private Set<GitRepoMember> gitRepoMembers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "spark_line", joinColumns = @JoinColumn(name = "git_repo_id"))
    private List<Integer> sparkLine = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitRepo(String name) {
        this.name = name;
    }

    public void updateClosedIssueNum(Integer closedIssueNum) {
        this.closedIssueNum = closedIssueNum;
    }

    public void organizeGitRepoMember(GitRepoMember gitRepoMember) {
        this.gitRepoMembers.add(gitRepoMember);
    }

    public void updateSparkLine(List<Integer> sparkLine) {
        this.sparkLine = sparkLine;
    }

}
