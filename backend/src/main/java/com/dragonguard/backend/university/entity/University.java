package com.dragonguard.backend.university.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class University {
    @Id @GeneratedValue
    private Long id;
}
