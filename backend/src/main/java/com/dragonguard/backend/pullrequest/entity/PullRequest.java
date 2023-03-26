package com.dragonguard.backend.pullrequest.entity;

import com.dragonguard.backend.global.audit.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 김승진
 * @description 깃허브의 PullRequest 정보를 담는 DB Entity
 */

@Entity
@SoftDelete
public class PullRequest {
    @Id
    @GeneratedValue
    private Long id;
}
