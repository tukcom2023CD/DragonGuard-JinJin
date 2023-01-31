package com.dragonguard.backend.commit.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
