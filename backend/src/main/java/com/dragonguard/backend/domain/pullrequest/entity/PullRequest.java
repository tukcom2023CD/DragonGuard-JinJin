package com.dragonguard.backend.domain.pullrequest.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PullRequest implements Auditable {

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
        this.member = member;
        this.amount = amount;
        this.year = year;
        organizeMember();
    }

    public void updatePullRequestNum(final Integer amount) {
        this.amount = amount;
    }

    private void organizeMember() {
        this.member.addPullRequest(this);
    }

    public boolean isNotUpdatable(final Integer amount) {
        return Optional.ofNullable(this.baseTime.getUpdatedAt()).orElseGet(() -> this.baseTime.getCreatedAt()).isAfter(LocalDateTime.now().minusSeconds(20L))
                || this.amount.intValue() == amount.intValue();
    }
}
