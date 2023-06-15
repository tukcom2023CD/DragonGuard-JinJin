package com.dragonguard.backend.support.fixture.blockchain.entity;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
public enum BlockchainFixture {
    ONE_COMMIT(ContributeType.COMMIT, BigInteger.ONE),
    TWO_ISSUES(ContributeType.ISSUE, BigInteger.TWO);

    ContributeType contributeType;
    BigInteger amount;


    public Blockchain toEntity(Member member) {
        return new Blockchain(contributeType, amount, member);
    }
}
