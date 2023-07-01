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
    List<Blockchain> findAllByMember(Member member);
    List<Blockchain> findAllByMemberId(UUID memberId);
    boolean existsByMemberId(UUID memberId);
    Blockchain save(Blockchain blockchain);
    Optional<Blockchain> findById(Long id);
    List<Blockchain> findAllByMemberAndContributeType(Member member, ContributeType contributeType);
}
