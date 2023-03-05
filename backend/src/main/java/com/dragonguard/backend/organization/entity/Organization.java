package com.dragonguard.backend.organization.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) 정보를 담는 DB Entity
 */

@Entity
@SoftDelete
public class Organization {
    @Id @GeneratedValue
    private Long id;
}
