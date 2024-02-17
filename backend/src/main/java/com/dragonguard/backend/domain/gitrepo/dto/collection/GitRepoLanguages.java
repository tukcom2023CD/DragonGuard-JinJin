package com.dragonguard.backend.domain.gitrepo.dto.collection;

import java.util.IntSummaryStatistics;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 repository의 언어 사용 정보를 나타내는 map에 대한 일급 컬렉션
 */
@Getter
@RequiredArgsConstructor
public class GitRepoLanguages {
    private static final int NO_CONTRIBUTION = 0;
    private final Map<String, Integer> languages;

    public IntSummaryStatistics getStatistics() {
        if (languages.keySet().isEmpty()) {
            return new IntSummaryStatistics(
                    NO_CONTRIBUTION, NO_CONTRIBUTION, NO_CONTRIBUTION, NO_CONTRIBUTION);
        }
        return languages.keySet().stream().mapToInt(languages::get).summaryStatistics();
    }
}
