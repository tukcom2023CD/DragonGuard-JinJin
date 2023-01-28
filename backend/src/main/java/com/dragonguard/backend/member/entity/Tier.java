package com.dragonguard.backend.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Tier {
    UNKNOWN(0),
    BRONZE(50),
    SILVER(200),
    GOLD(500),
    PLATINUM(1000),
    DIAMOND(3000),
    RUBY(7000);

    private final Integer minCommits;

    public static Tier checkTier(Integer commitNum) {
        return Arrays.stream(values())
                .filter(i -> i.getMinCommits() > commitNum)
                .findFirst()
                .orElseThrow(TierNoneMatchException::new);
    }
}
