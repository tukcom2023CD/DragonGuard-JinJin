package com.dragonguard.backend.config.blockchain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 블록체인 관련 정보를 임시 저장하는 클래스
 */

@Getter
@AllArgsConstructor
public class BlockchainJson {
    private String abiJson;
    private String keyRingJson;
}
