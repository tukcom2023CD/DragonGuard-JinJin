package com.dragonguard.backend.domain.gitrepo.dto.collection;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 Repository 기여자들의 기여 정보를 담는 일급 컬렉션
 */
@RequiredArgsConstructor
public class GitRepoContributions {
    private final Map<GitRepoMemberClientResponse, Integer> contributions;

    public Integer getContributionByKey(GitRepoMemberClientResponse key) {
        return contributions.get(key);
    }
}
