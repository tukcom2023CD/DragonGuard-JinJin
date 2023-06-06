package com.dragonguard.backend.domain.blockchain.repository;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockchainRepository {
    List<Blockchain> findAllByMemberId(UUID memberId);
    boolean existsByMemberId(UUID memberId);
    Blockchain save(Blockchain blockchain);
    Optional<Blockchain> findById(Long id);
}
