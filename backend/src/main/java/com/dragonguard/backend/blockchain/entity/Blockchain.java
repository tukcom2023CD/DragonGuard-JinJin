package com.dragonguard.backend.blockchain.entity;

import com.dragonguard.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blockchain {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContributeType contributeType;

    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Blockchain(ContributeType contributeType, Integer amount, Member member) {
        this.contributeType = contributeType;
        this.amount = amount;
        this.member = member;
    }
}
