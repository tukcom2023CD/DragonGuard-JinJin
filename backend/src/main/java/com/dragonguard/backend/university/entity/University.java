package com.dragonguard.backend.university.entity;

import com.dragonguard.backend.global.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@SoftDelete
public class University {
    @Id @GeneratedValue
    private Long id;
}
