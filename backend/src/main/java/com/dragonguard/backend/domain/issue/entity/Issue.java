package com.dragonguard.backend.domain.issue.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import com.dragonguard.backend.global.template.entity.Contribution;

import lombok.*;

import javax.persistence.*;

/**
 * @author 김승진
 * @description issue 정보를 담는 DB Entity
 */
@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue implements Contribution {

    @Id @GeneratedValue private Long id;

    @JoinColumn(columnDefinition = "BINARY(16)")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
    public Issue(final Member member, final Integer amount, final Integer year) {
        if (amount < 0) return;
        this.amount = amount;
        this.year = year;
        organizeMember(member);
    }

    @Override
    public void updateContributionNum(final Integer amount) {
        this.amount = amount;
    }

    private void organizeMember(final Member member) {
        this.member = member;
        this.member.addIssue(this);
    }

    @Override
    public boolean isNotUpdatable(final Integer amount) {
        return this.amount.intValue() == amount.intValue();
    }
}
