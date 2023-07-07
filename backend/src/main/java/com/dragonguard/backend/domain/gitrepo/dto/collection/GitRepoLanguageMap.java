package com.dragonguard.backend.domain.gitrepo.dto.collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.IntSummaryStatistics;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 repository의 언어 사용 정보를 나타내는 map에 대한 일급 컬렉션
 */

@Getter
@RequiredArgsConstructor
public class GitRepoLanguageMap {
    private final Map<String, Integer> languages;

    public IntSummaryStatistics getStatistics() {
        return languages.keySet().isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0)
                : languages.keySet().stream().mapToInt(languages::get).summaryStatistics();
    }
}
