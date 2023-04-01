package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.IntSummaryStatistics;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 응답 정보를 담는 dto
 */

@Getter
@ToString
@AllArgsConstructor
public class GitRepoResponse {
    private GitRepoClientResponse gitRepo;
    private StatisticsResponse statistics;
    private Map<String, Integer> languages;
    private IntSummaryStatistics languagesStats;
}
