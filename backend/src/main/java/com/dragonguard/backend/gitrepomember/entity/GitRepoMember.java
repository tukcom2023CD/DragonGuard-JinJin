package com.dragonguard.backend.gitrepomember.entity;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.global.BaseTime;
import com.dragonguard.backend.global.SoftDelete;
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
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "git_repo_id")
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

    public void update(GitRepoMember gitRepoMember) {
        this.gitRepo = gitRepoMember.gitRepo;
        this.member = gitRepoMember.member;
        this.commits = gitRepoMember.commits;
        this.additions = gitRepoMember.additions;
        this.deletions = gitRepoMember.deletions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitRepoMember that = (GitRepoMember) o;
        return Objects.equals(gitRepo, that.gitRepo)
                && Objects.equals(member, that.member)
                && Objects.equals(commits, that.commits)
                && Objects.equals(additions, that.additions)
                && Objects.equals(deletions, that.deletions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gitRepo, member, commits, additions, deletions);
    }
}
