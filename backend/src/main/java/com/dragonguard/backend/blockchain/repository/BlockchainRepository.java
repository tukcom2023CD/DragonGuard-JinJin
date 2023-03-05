package com.dragonguard.backend.blockchain.repository;

import com.dragonguard.backend.blockchain.entity.Blockchain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 김승진
 * @description 블록체인 관련 DB와의 CRUD를 담당하는 클래스
 */

@Repository
public interface BlockchainRepository extends JpaRepository<Blockchain, Long> {
    List<Blockchain> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
