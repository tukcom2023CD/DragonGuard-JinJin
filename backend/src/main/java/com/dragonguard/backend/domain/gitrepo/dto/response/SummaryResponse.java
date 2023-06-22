package com.dragonguard.backend.domain.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.IntSummaryStatistics;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponse {
    private Long count;
    private Long sum;
    private Integer min;
    private Integer max;
    private Double average;

    public SummaryResponse(IntSummaryStatistics statistics) {
        this.count = statistics.getCount();
        this.sum = statistics.getSum();
        this.min = statistics.getMin();
        this.max = statistics.getMax();
        this.average = statistics.getAverage();
    }
}
