package com.dragonguard.backend.domain.gitrepo.entity;

import lombok.Getter;

import java.util.IntSummaryStatistics;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 repository의 언어 사용 정보를 나타내는 map에 대한 일급 컬렉션
 */

@Getter
public class GitRepoLanguage {
    private final Map<String, Integer> languages;

    public GitRepoLanguage(Map<String, Integer> languages) {
        this.languages = languages;
    }

    public IntSummaryStatistics getStatistics() {
        return languages.keySet().isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0)
                : languages.keySet().stream().mapToInt(languages::get).summaryStatistics();
    }
}
