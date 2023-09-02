package com.dragonguard.backend.domain.commit.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author 김승진
 * @description 커밋 정보를 담는 DB Entity
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
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

    @Version
    private Long version;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Commit(Integer year, Integer amount, Member member) {
        if (amount < 0) return;
        this.year = year;
        this.amount = amount;
        this.member = member;
        organize();
    }

    public boolean customEqualsWithAmount(Commit commit) {
        return year.intValue() == commit.year && amount.intValue() == commit.amount.intValue()
                && member.getGithubId().equals(commit.member.getGithubId());
    }

    private void organize() {
        member.addCommit(this);
    }

    public void updateCommitNum(Integer commitNum) {
        this.amount = commitNum;
    }

    public boolean isNotUpdatable(Integer amount) {
        return updatedCurrently() || this.amount.intValue() == amount.intValue();
    }

    public boolean updatedCurrently() {
        return Optional.ofNullable(this.baseTime.getUpdatedAt()).orElseGet(() -> this.baseTime.getCreatedAt()).isAfter(LocalDateTime.now().minusSeconds(20L));
    }
}
