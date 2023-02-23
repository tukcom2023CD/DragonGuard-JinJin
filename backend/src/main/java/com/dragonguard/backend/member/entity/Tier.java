package com.dragonguard.backend.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Tier {
    SPROUT(0, "새싹"),
    BRONZE(50, "브론즈"),
    SILVER(200, "실버"),
    GOLD(500, "골드"),
    PLATINUM(1000, "플레티넘"),
    DIAMOND(3000, "다이아몬드"),
    RUBY(5000, "루비"),
    MASTER(10000, "마스터");

    private final Integer maxCommits;
    private final String korean;

    public static Tier checkTier(Long amount) {
        return Arrays.stream(values())
                .filter(i -> i.getMaxCommits() >= amount)
                .findFirst()
                .orElseThrow(TierNoneMatchException::new);
    }
}
