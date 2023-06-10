package com.dragonguard.backend.domain.issue.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author 김승진
 * @description issue 정보를 담는 DB Entity
 */

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
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
        this.member = member;
        this.amount = amount;
        this.year = year;
        organize(member);
    }

    public void updateIssueNum(Integer amount) {
        this.amount = amount;
    }

    public boolean customEqualsWithAmount(Issue issue) {
        return Objects.equals(member.getGithubId(), issue.member.getGithubId()) && Objects.equals(amount, issue.amount) && Objects.equals(year, issue.year);
    }

    public boolean customEquals(Issue issue) {
        return year.equals(issue.year) && member.getGithubId().equals(issue.member.getGithubId());
    }

    private void organize(Member member) {
        member.addIssue(this);
    }
}
