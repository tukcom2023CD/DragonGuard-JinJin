package com.dragonguard.backend.organization.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@SoftDelete
public class Organization {
    @Id @GeneratedValue
    private Long id;
}
