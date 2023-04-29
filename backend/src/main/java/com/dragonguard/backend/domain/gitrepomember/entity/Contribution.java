package com.dragonguard.backend.domain.gitrepomember.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contribution {
    private Integer commits;

    private Integer additions;

    private Integer deletions;
}
