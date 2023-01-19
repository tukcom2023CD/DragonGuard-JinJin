package com.dragonguard.backend.issue.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Issue {
    @Id @GeneratedValue
    private Long id;
}
