package com.dragonguard.backend.domain.gitrepo.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 레포지토리 스파크라인 정보를 담는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoSparkLineResponse {
    private Integer[] all;
}
