package com.dragonguard.backend.domain.gitrepomember.entity;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;

import lombok.*;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 깃허브 Repository 구성원 정보를 담는 DB Entity
 */
@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@EqualsAndHashCode(of = {"gitRepo", "member"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepoMember implements Auditable {

    @Id @GeneratedValue private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private GitRepo gitRepo;

    @JoinColumn(columnDefinition = "BINARY(16)")
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Member member;

    @Embedded private GitRepoContribution gitRepoContribution;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitRepoMember(
            final GitRepo gitRepo,
            final Member member,
            final GitRepoContribution gitRepoContribution) {
        this.gitRepo = gitRepo;
        this.member = member;
        this.gitRepoContribution = gitRepoContribution;
        organize();
    }

    public void organize() {
        this.gitRepo.organizeGitRepoMember(this);
        this.member.organizeGitRepoMember(this);
    }

    public void update(final GitRepoMember gitRepoMember) {
        this.gitRepo = gitRepoMember.gitRepo;
        this.member = gitRepoMember.member;
        this.gitRepoContribution = gitRepoMember.gitRepoContribution;
    }

    public void updateGitRepoContribution(
            final Integer commits, final Integer additions, final Integer deletions) {
        this.gitRepoContribution = new GitRepoContribution(commits, additions, deletions);
    }

    public void updateProfileImageAndContribution(
            final String profileUrl,
            final Integer commits,
            final Integer additions,
            final Integer deletions) {
        this.member.updateProfileImage(profileUrl);
        updateGitRepoContribution(commits, additions, deletions);
    }
}
