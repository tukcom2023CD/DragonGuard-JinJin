package com.dragonguard.backend.blockchain.repository;

import com.dragonguard.backend.blockchain.entity.Blockchain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockchainRepository extends JpaRepository<Blockchain, Long> {
}
