package com.dragonguard.backend.domain.pullrequest.entity;

import com.dragonguard.backend.domain.member.entity.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "BINARY(16)")
    private Member member;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer year;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public PullRequest(Member member, Integer amount, Integer year) {
        this.member = member;
        this.amount = amount;
        this.year = year;
        organize();
    }

    public void updatePullRequestNum(Integer amount) {
        this.amount = amount;
    }

    public boolean customEqualsWithAmount(PullRequest pullRequest) {
        return Objects.equals(member.getGithubId(), pullRequest.member.getGithubId()) && Objects.equals(amount, pullRequest.amount) && Objects.equals(year, pullRequest.year);
    }

    public boolean customEquals(PullRequest pullRequest) {
        return year.equals(pullRequest.year) && member.getGithubId().equals(pullRequest.member.getGithubId());
    }

    private void organize() {
        this.member.addPullRequest(this);
    }
}
