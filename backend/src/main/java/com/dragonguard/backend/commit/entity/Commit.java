package com.dragonguard.backend.commit.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Commit extends BaseTime {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer year;
    @Column(nullable = false)
    private Integer commitNum;

    private String githubId;

    @Builder
    public Commit(Integer year, Integer commitNum, String githubId) {
        this.year = year;
        this.commitNum = commitNum;
        this.githubId = githubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return year.equals(commit.year) && commitNum.equals(commit.commitNum) && githubId.equals(commit.githubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, commitNum, githubId);
    }

    public boolean customEquals(Commit commit) {
        return year.equals(commit.year) && githubId.equals(commit.githubId);
    }
}
