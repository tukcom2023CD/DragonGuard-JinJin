package com.dragonguard.backend.gitrepomember.entity;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import com.dragonguard.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author 김승진
 * @description 깃허브 Repository 구성원 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepoMember extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "git_repo_id")
    private GitRepo gitRepo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    private Contribution contribution;

    @Builder
    public GitRepoMember(GitRepo gitRepo, Member member, Contribution contribution) {
        this.gitRepo = gitRepo;
        this.member = member;
        this.contribution = contribution;
    }

    public void update(GitRepoMember gitRepoMember) {
        this.gitRepo = gitRepoMember.gitRepo;
        this.member = gitRepoMember.member;
        this.contribution = gitRepoMember.contribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitRepoMember that = (GitRepoMember) o;
        return Objects.equals(gitRepo, that.gitRepo) && Objects.equals(member, that.member) && Objects.equals(contribution, that.contribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gitRepo, member, contribution);
    }
}
