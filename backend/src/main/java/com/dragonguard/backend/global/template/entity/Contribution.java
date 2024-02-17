package com.dragonguard.backend.global.template.entity;

import com.dragonguard.backend.global.audit.Auditable;

public interface Contribution extends Auditable {
    void updateContributionNum(final Integer amount);

    boolean isNotUpdatable(final Integer amount);
}
