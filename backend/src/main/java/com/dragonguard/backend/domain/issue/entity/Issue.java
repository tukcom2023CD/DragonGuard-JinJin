package com.dragonguard.backend.domain.issue.entity;

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

    @Column(nullable = false, unique = true)
    private String githubId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer year;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Issue(String githubId, Integer amount, Integer year) {
        this.githubId = githubId;
        this.amount = amount;
        this.year = year;
    }

    public void updateIssueNum(Integer amount) {
        this.amount = amount;
    }

    public boolean customEquals(Issue issue) {
        return year.equals(issue.year) && githubId.equals(issue.githubId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(githubId, issue.githubId) && Objects.equals(amount, issue.amount) && Objects.equals(year, issue.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(githubId, amount, year);
    }
}
