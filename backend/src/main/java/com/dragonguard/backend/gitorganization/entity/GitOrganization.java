package com.dragonguard.backend.gitorganization.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@SoftDelete
public class GitOrganization {
    @Id @GeneratedValue
    private Long id;
}
