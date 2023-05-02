package com.dragonguard.backend.domain.gitrepo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author 김승진
 * @description 깃허브 Repository 비교 요청 정보를 담는 dto
 */

@Getter
@ToString // Redis를 위해 넣은 toString
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoCompareRequest {
    @NotBlank
    private String firstRepo;
    @NotBlank
    private String secondRepo;
}
