package com.dragonguard.backend.domain.pullrequest.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import com.dragonguard.backend.global.template.entity.Contribution;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author 김승진
 * @description 깃허브의 PullRequest 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PullRequest implements Contribution {

    private static final Long UPDATE_TIME_UNIT = 20L;

    @Id
    @GeneratedValue
    private Long id;

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
    public PullRequest(final Member member, final Integer amount, final Integer year) {
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
        this.member.addPullRequest(this);
    }

    @Override
    public boolean isNotUpdatable(final Integer amount) {
        return updatedCurrently() || this.amount.intValue() == amount.intValue();
    }

    private boolean updatedCurrently() {
        return Optional.ofNullable(this.baseTime.getUpdatedAt()).orElseGet(() -> this.baseTime.getCreatedAt()).isAfter(LocalDateTime.now().minusSeconds(UPDATE_TIME_UNIT));
    }
}
