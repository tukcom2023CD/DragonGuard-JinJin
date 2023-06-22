package com.dragonguard.backend.domain.gitrepo.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 김승진
 * @description 레포지토리 내부 기여자들의 정보를 담는 dto 클래스
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoKafkaResponse {
    private List<GitRepoMemberDetailsResponse> result;
}
