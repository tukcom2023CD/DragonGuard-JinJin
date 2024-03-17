package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 레포지토리 내부 기여자 한 명의 정보를 담는 dto 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberDetailsResponse {
    private String gitRepo;
    private String member;
    private Integer commits;
    private Integer addition;
    private Integer deletion;
}
