package com.dragonguard.backend.domain.pullrequest.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;

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

    @JoinColumn(columnDefinition = "BINARY(16)")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
        if (amount < 0) return;
        this.member = member;
        this.amount = amount;
        this.year = year;
        organize();
    }

    public void updatePullRequestNum(Integer amount) {
        this.amount = amount;
    }

    public boolean customEqualsWithAmount(PullRequest pullRequest) {
        return member.getGithubId().equals(pullRequest.member.getGithubId()) && amount.intValue() == pullRequest.amount.intValue() && year.intValue() ==  pullRequest.year.intValue();
    }

    public boolean customEquals(PullRequest pullRequest) {
        return year.intValue() == pullRequest.year.intValue() && member.getGithubId().equals(pullRequest.member.getGithubId());
    }

    private void organize() {
        this.member.addPullRequest(this);
    }
}
