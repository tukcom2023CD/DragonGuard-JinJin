package com.dragonguard.backend.domain.codereview.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 코드리뷰 정보를 담는 엔티티 클래스
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeReview implements Auditable {
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
    public CodeReview(Integer year, Integer amount, Member member) {
        if (amount < 0) return;
        this.year = year;
        this.amount = amount;
        this.member = member;
        organize();
    }

    private void organize() {
        this.member.addCodeReview(this);
    }

    public void updateCodeReviewNum(Integer codeReviewNum) {
        this.amount = codeReviewNum;
    }

    public boolean customEqualsWithAmount(CodeReview codeReview) {
        return year.intValue() == codeReview.year && amount.intValue() == codeReview.amount.intValue()
                && member.getGithubId().equals(codeReview.member.getGithubId());
    }
}
