package com.dragonguard.backend.commit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 커밋 관련 응답 정보를 담는 dto
 */

@Getter
@AllArgsConstructor
public class CommitScrapingResponse {
    String githubId;
    Integer commitNum;
}
