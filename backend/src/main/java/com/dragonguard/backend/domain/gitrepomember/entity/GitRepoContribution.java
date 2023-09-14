package com.dragonguard.backend.domain.gitrepomember.entity;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * @author 김승진
 * @description 레포지토리 내부 기여자들의 기여도를 가지는 embeddable 클래스
 */

@Getter
@Embeddable
@AllArgsConstructor
@EqualsAndHashCode(of = {"commits", "additions", "deletions"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepoContribution {
    private Integer commits;
    private Integer additions;
    private Integer deletions;
}
