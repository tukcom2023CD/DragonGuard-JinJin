package com.dragonguard.backend.commit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Commit {
    @Id @GeneratedValue
    private Long id;
}
