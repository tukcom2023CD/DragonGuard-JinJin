package com.dragonguard.backend.gitorganization.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GitOrganization {
    @Id @GeneratedValue
    private Long id;
}
