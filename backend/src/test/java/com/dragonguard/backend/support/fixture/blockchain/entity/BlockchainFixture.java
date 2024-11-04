package com.dragonguard.backend.support.fixture.blockchain.entity;

import com.dragonguard.backend.domain.member.entity.ContributeType;

import lombok.AllArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
public enum BlockchainFixture {
    ONE_COMMIT(ContributeType.COMMIT, BigInteger.ONE, "321321"),
    TWO_ISSUES(ContributeType.ISSUE, BigInteger.TWO, "123123");

    ContributeType contributeType;
    BigInteger amount;
    String transactionHash;
}
