package com.dragonguard.backend.domain.gitrepomember.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 멤버 정보 리스트를 Github REST API에서 응답을 받아 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberListClientResponse {
    private List<GitRepoMemberClientResponse> members = new ArrayList<>();
}
