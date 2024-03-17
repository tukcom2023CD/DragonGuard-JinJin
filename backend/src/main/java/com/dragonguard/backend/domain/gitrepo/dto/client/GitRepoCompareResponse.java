package com.dragonguard.backend.domain.gitrepo.dto.client;

import com.dragonguard.backend.domain.gitrepo.dto.response.StatisticsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.SummaryResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 응답 정보를 담는 dto
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoCompareResponse {
    private GitRepoClientResponse gitRepo;
    private StatisticsResponse statistics;
    private Map<String, Integer> languages;
    private SummaryResponse languagesStats;
    private List<String> profileUrls;
}
