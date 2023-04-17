package com.dragonguard.backend.issue.entity;

import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author 김승진
 * @description issue 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Issue extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String githubId;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private Integer year;

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
