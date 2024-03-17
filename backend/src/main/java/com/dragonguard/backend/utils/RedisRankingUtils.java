package com.dragonguard.backend.utils;

import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.InvalidJsonOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 랭킹 관련 Redis에 대한 요청을 담당하는 클래스
 */
@Service
@RequiredArgsConstructor
public class RedisRankingUtils {

    private static final String USER_RANKING = "userRanking";
    private static final String MEMBER_DETAILS = "memberDetails:";
    private final RedisTemplate<String, String> redisTemplate;

    public void addUserScore(final Member member) {
        redisTemplate
                .opsForZSet()
                .add(USER_RANKING, member.getId().toString(), member.getSumOfTokens());

        final MemberRankResponse memberDetails =
                new MemberRankResponse(
                        member.getId(),
                        member.getName(),
                        member.getGithubId(),
                        member.getSumOfTokens(),
                        member.getTier(),
                        member.getProfileImage());

        redisTemplate
                .opsForValue()
                .set(getMemberDetailsKey(member.getId()), convertToJson(memberDetails));
    }

    private String getMemberDetailsKey(final UUID memberId) {
        return MEMBER_DETAILS + memberId.toString();
    }

    private String convertToJson(final MemberRankResponse memberDetails) {
        try {
            return new ObjectMapper().writeValueAsString(memberDetails);
        } catch (final JsonProcessingException e) {
            throw new InvalidJsonOperationException();
        }
    }

    public List<MemberRankResponse> getTopUsers(final long start, final long end) {
        return redisTemplate.opsForZSet().reverseRange(USER_RANKING, start, end).stream()
                .map(UUID::fromString)
                .map(this::getMemberDetailsFromRedis)
                .collect(Collectors.toList());
    }

    private MemberRankResponse getMemberDetailsFromRedis(final UUID memberId) {
        String memberDetailsJson = redisTemplate.opsForValue().get(getMemberDetailsKey(memberId));
        return convertFromJson(memberDetailsJson);
    }

    private MemberRankResponse convertFromJson(final String memberDetailsJson) {
        try {
            return new ObjectMapper().readValue(memberDetailsJson, MemberRankResponse.class);
        } catch (final Exception e) {
            throw new InvalidJsonOperationException();
        }
    }
}
