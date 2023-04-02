package com.dragonguard.backend.gitrepomember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 멤버의 addition, deletion, commits 정보를 Github REST API에서 응답을 받아 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Week {
    private Integer a;
    private Integer d;
    private Integer c;
}
