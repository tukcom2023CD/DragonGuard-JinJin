package com.dragonguard.backend.blockchain.repository;

import com.dragonguard.backend.blockchain.entity.Blockchain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockchainRepository extends JpaRepository<Blockchain, Long> {
    List<Blockchain> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
