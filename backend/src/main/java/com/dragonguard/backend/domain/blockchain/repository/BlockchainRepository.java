package com.dragonguard.backend.domain.blockchain.repository;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 김승진
 * @description 블록체인 테이블의 DB 접근을 수행하는 로직을 가진 인터페이스
 */

public interface BlockchainRepository {
    Blockchain save(Blockchain blockchain);
    Optional<Blockchain> findById(Long id);
    Optional<Blockchain> findByMemberAndContributeType(Member member, ContributeType contributeType);
    List<Blockchain> findByMemberId(UUID memberId);
}
