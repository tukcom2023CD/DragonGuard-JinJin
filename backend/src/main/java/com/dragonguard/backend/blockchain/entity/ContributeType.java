package com.dragonguard.backend.blockchain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 기여 종류를 구별하는 enum
 */

@Getter
@AllArgsConstructor
public enum ContributeType {
    COMMIT, ISSUE, PULL_REQUEST
}
