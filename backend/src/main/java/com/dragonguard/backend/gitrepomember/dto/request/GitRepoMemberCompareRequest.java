package com.dragonguard.backend.gitrepomember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 김승진
 * @description 깃허브 Repository 멤버 비교를 요청하는 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberCompareRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String firstRepo;
    @NotBlank
    private String secondName;
    @NotBlank
    private String secondRepo;
}
