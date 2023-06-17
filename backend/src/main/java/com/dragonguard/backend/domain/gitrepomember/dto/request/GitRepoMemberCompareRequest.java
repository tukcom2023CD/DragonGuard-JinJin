package com.dragonguard.backend.domain.gitrepomember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버 비교를 요청하는 정보를 담는 dto
 */

@Getter
@ToString // Redis를 위해 넣은 toString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberCompareRequest {
    @NotBlank
    private String firstGithubId;
    @NotBlank
    private String firstRepo;
    @NotBlank
    private String secondGithubId;
    @NotBlank
    private String secondRepo;
}
