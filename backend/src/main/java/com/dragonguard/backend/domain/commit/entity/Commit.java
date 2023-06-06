package com.dragonguard.backend.domain.commit.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author 김승진
 * @description 커밋 정보를 담는 DB Entity
 */

@Getter
@Entity
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

    private String githubId;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Commit(Integer year, Integer amount, String githubId) {
        this.year = year;
        this.amount = amount;
        this.githubId = githubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return year.equals(commit.year) && amount.equals(commit.amount) && githubId.equals(commit.githubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, amount, githubId);
    }

    public boolean customEquals(Commit commit) {
        return year.equals(commit.year) && githubId.equals(commit.githubId);
    }
}
