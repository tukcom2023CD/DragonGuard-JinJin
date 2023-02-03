package com.dragonguard.backend.member.repository;

import com.dragonguard.backend.member.entity.Member;
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
    @Override
    public List<Member> findRanking(Pageable pageable) {
        return jpaQueryFactory
                .select(member)
                .from(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberOrderConverter.convert(pageable.getSort()))
                .fetch();
    }
}
