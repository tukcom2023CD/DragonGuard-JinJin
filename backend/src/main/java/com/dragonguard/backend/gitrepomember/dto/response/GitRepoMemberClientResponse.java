package com.dragonguard.backend.gitrepomember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 멤버 전체적인 정보를 Github REST API에서 응답을 받아 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoMemberClientResponse {
    private Integer total;
    private Week[] weeks;
    private Author author;
}
