package com.dragonguard.backend.domain.blockchain.repository;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;

import java.util.List;
import java.util.UUID;

public interface BlockchainRepository {
    List<Blockchain> findByMemberId(UUID memberId);

    boolean existsByMemberId(UUID memberId);

    Blockchain save(Blockchain blockchain);
}
