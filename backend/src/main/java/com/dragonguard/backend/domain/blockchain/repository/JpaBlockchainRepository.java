package com.dragonguard.backend.domain.blockchain.repository;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author 김승진
 * @description 블록체인 관련 DB와의 CRUD를 담당하는 클래스
 */

public interface JpaBlockchainRepository extends JpaRepository<Blockchain, Long>, BlockchainRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Blockchain b WHERE b.member = :member")
    List<Blockchain> findAllByMember(Member member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Blockchain> findAllByMemberAndContributeType(Member member, ContributeType contributeType);
}
