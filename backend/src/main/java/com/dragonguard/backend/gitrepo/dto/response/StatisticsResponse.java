package com.dragonguard.backend.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.IntSummaryStatistics;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private IntSummaryStatistics commitStats;
    private IntSummaryStatistics additionStats;
    private IntSummaryStatistics deletionStats;
}
