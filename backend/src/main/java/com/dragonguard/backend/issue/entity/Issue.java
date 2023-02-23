package com.dragonguard.backend.issue.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@SoftDelete
public class Issue {
    @Id @GeneratedValue
    private Long id;
}
