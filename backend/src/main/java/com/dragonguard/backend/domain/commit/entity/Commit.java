package com.dragonguard.backend.domain.commit.entity;

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
 * @description 커밋 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Commit implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer amount;

    @JoinColumn(columnDefinition = "BINARY(16)")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Member member;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Commit(final Integer year, final Integer amount, final Member member) {
        if (amount < 0) return;
        this.year = year;
        this.amount = amount;
        organizeMember(member);
    }

    private void organizeMember(final Member member) {
        this.member = member;
        this.member.addCommit(this);
    }

    public void updateCommitNum(final Integer amount) {
        this.amount = amount;
    }

    public boolean isNotUpdatable(final Integer amount) {
        return updatedCurrently() || this.amount.intValue() == amount.intValue();
    }

    public boolean updatedCurrently() {
        return Optional.ofNullable(this.baseTime.getUpdatedAt()).orElseGet(() -> this.baseTime.getCreatedAt()).isAfter(LocalDateTime.now().minusSeconds(20L));
    }
}
