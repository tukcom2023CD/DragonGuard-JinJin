package com.dragonguard.backend.domain.member.entity;

import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 멤버 티어를 구분 및 계산하는 enum
 */
@Getter
@RequiredArgsConstructor
public enum Tier {
    SPROUT(i -> i < 50L, "새싹"),
    BRONZE(i -> 50L <= i && i < 200L, "브론즈"),
    SILVER(i -> 200L <= i && i < 500L, "실버"),
    GOLD(i -> 500L <= i && i < 1000L, "골드"),
    PLATINUM(i -> 1000L <= i && i < 3000L, "플레티넘"),
    DIAMOND(i -> 3000L <= i && i < 5000L, "다이아몬드"),
    RUBY(i -> 5000L <= i && i < 10000L, "루비"),
    MASTER(i -> 10000L <= i, "마스터");

    private final Predicate<Long> tierPredicate;
    private final String korean;

    public static Tier checkTier(final long amount) {
        return Arrays.stream(values())
                .filter(t -> t.getTierPredicate().test(amount))
                .findFirst()
                .orElse(Tier.SPROUT);
    }
}
