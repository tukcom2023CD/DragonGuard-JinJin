package com.dragonguard.backend.domain.member.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 멤버 레포지토리 Client 정보를 갖는 dto 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryClientResponse {
    private String githubId;
}
