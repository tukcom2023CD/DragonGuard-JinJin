package com.dragonguard.backend.issue.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 김승진
 * @description issue 정보를 담는 DB Entity
 */

@Entity
@SoftDelete
public class Issue {
    @Id @GeneratedValue
    private Long id;
}
