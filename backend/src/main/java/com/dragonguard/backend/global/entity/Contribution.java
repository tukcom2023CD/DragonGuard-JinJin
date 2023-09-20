package com.dragonguard.backend.global.entity;

public interface Contribution {
    static final Long UPDATE_TIME_UNIT = 20L;
    void updateContributionNum(final Integer amount);
    boolean isNotUpdatable(final Integer amount);
}
