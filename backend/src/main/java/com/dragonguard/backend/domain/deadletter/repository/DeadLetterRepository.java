package com.dragonguard.backend.domain.deadletter.repository;

import com.dragonguard.backend.domain.deadletter.entity.DeadLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 김승진
 * @description Kafka의 Dead Letter를 DB에 저장, 조회를 수행하는 클래스
 */


public interface DeadLetterRepository extends JpaRepository<DeadLetter, Long> {
    List<DeadLetter> findAllByIsRetriedIsFalse();
}
