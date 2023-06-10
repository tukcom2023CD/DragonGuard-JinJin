package com.dragonguard.backend.domain.gitrepomember.entity;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author 김승진
 * @description 깃허브 Repository 구성원 정보를 담는 DB Entity
 */

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepoMember implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private GitRepo gitRepo;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    private GitRepoContribution gitRepoContribution;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitRepoMember(GitRepo gitRepo, Member member, GitRepoContribution gitRepoContribution) {
        this.gitRepo = gitRepo;
        this.member = member;
        this.gitRepoContribution = gitRepoContribution;
    }

    public void update(GitRepoMember gitRepoMember) {
        this.gitRepo = gitRepoMember.gitRepo;
        this.member = gitRepoMember.member;
        this.gitRepoContribution = gitRepoMember.gitRepoContribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitRepoMember that = (GitRepoMember) o;
        return Objects.equals(gitRepo, that.gitRepo) && Objects.equals(member, that.member) && Objects.equals(gitRepoContribution, that.gitRepoContribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gitRepo, member, gitRepoContribution);
    }
}
