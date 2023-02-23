package com.dragonguard.backend.blockchain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContributeType {
    COMMIT, ISSUE, PULL_REQUEST
}
