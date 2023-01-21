package com.dragonguard.backend.gitrepo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GitRepo {
    @Id @GeneratedValue
    private Long id;
}
