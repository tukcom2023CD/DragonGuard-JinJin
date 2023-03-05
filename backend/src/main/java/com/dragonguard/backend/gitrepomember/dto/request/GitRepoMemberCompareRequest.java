package com.dragonguard.backend.gitrepomember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버 비교를 요청하는 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberCompareRequest {
    private String firstName;
    private String firstRepo;
    private String secondName;
    private String secondRepo;
}
