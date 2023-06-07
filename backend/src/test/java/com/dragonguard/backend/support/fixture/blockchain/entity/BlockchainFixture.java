package com.dragonguard.backend.support.fixture.blockchain.entity;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
public enum BlockchainFixture {
    SAMPLE1(ContributeType.COMMIT, BigInteger.ONE),
    SAMPLE2(ContributeType.ISSUE, BigInteger.TWO);

    ContributeType contributeType;
    BigInteger amount;


    public Blockchain toEntity(Member member) {
        return new Blockchain(contributeType, amount, member);
    }
}
