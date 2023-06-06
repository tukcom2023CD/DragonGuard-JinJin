package com.dragonguard.backend.domain.pullrequest.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author 김승진
 * @description 깃허브의 PullRequest 정보를 담는 DB Entity
 */

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PullRequest implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String githubId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer year;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public PullRequest(String githubId, Integer amount, Integer year) {
        this.githubId = githubId;
        this.amount = amount;
        this.year = year;
    }

    public void updatePullRequestNum(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PullRequest that = (PullRequest) o;
        return Objects.equals(githubId, that.githubId) && Objects.equals(amount, that.amount) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(githubId, amount, year);
    }

    public boolean customEquals(PullRequest pullRequest) {
        return year.equals(pullRequest.year) && githubId.equals(pullRequest.githubId);
    }
}
