package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 응답 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoResponse {
    private GitRepoClientResponse gitRepo;
    private StatisticsResponse statistics;
    private Map<String, Integer> languages = new HashMap<>();
    private IntSummaryStatistics languagesStats;
}
