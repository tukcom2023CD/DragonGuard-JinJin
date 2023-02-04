package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberQueryRepository {
    List<MemberRankResponse> findRanking(Pageable pageable);
}
