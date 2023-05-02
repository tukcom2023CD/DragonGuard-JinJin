package com.dragonguard.backend.pullrequest.entity;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 김승진
 * @description 깃허브의 PullRequest 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PullRequest {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String githubId;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private Integer year;

    @Builder
    public PullRequest(String githubId, Integer amount, Integer year) {
        this.githubId = githubId;
        this.amount = amount;
        this.year = year;
    }

    public void updatePullRequestNum(Integer amount) {
        this.amount = amount;
    }

    public boolean customEquals(PullRequest pullRequest) {
        return year.equals(pullRequest.year) && githubId.equals(pullRequest.githubId);
    }
}
