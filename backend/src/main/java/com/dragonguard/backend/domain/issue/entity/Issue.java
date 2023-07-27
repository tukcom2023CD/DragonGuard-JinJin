package com.dragonguard.backend.domain.issue.entity;

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
 * @description issue 정보를 담는 DB Entity
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue implements Auditable {

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
    public Issue(Member member, Integer amount, Integer year) {
        if (amount < 0) return;
        this.member = member;
        this.amount = amount;
        this.year = year;
        organize(member);
    }

    public void updateIssueNum(Integer amount) {
        this.amount = amount;
    }

    public boolean customEqualsWithAmount(Issue issue) {
        return member.getGithubId().equals(issue.member.getGithubId()) && amount.intValue() == issue.amount.intValue() && year.intValue() == issue.year.intValue();
    }

    private void organize(Member member) {
        member.addIssue(this);
    }

    public boolean isNotUpdatable(Integer amount) {
        return Optional.ofNullable(this.baseTime.getUpdatedAt()).orElseGet(() -> this.baseTime.getCreatedAt()).isAfter(LocalDateTime.now().minusSeconds(20L))
                || this.amount.intValue() == amount.intValue();
    }
}
