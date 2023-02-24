package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dragonguard.backend.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberOrderConverter memberOrderConverter;
    private final MemberQDtoFactory qDtoFactory;
    @Override
    public List<MemberRankResponse> findRanking(Pageable pageable) {
        return jpaQueryFactory
                .select(qDtoFactory.qMemberRankResponse())
                .from(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberOrderConverter.convert(pageable.getSort()))
                .fetch();
    }

    @Override
    public Integer findRankingById(Long id) {
        return jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.sumOfTokens.gt(
                        JPAExpressions
                        .select(member.sumOfTokens).from(member).where(member.id.eq(id))))
                .fetch().size() + 1;
    }

}
