package com.dragonguard.backend.blockchain.entity;

import com.dragonguard.backend.global.basetime.BaseTime;
import com.dragonguard.backend.global.basetime.SoftDelete;
import com.dragonguard.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * @author 김승진
 * @description 블록체인 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blockchain extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContributeType contributeType;

    private BigInteger amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String address;

    @Builder
    public Blockchain(ContributeType contributeType, BigInteger amount, Member member, String address) {
        this.contributeType = contributeType;
        this.amount = amount;
        this.member = member;
        this.address = address;
    }
}
