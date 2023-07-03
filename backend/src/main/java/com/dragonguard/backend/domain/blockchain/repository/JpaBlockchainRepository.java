package com.dragonguard.backend.domain.blockchain.repository;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 김승진
 * @description 블록체인 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaBlockchainRepository extends JpaRepository<Blockchain, Long>, BlockchainRepository {}
