package com.dragonguard.backend.domain.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberContribution {
    private Integer amount;
    private LocalDateTime time = LocalDateTime.now();
    private Boolean isConsumed = Boolean.FALSE;

    public MemberContribution(Integer amount) {
        this.amount = amount;
    }

    public void consumed() {
        this.isConsumed = Boolean.TRUE;
    }
}
