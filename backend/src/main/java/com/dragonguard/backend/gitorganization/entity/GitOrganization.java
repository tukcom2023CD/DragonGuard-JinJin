package com.dragonguard.backend.gitorganization.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 김승진
 * @description 깃허브의 Organization 정보를 담는 DB Entity
 */

@Entity
@SoftDelete
public class GitOrganization {
    @Id @GeneratedValue
    private Long id;
}
