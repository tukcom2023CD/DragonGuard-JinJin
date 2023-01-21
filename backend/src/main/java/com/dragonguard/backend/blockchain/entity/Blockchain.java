package com.dragonguard.backend.blockchain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Blockchain {
    @Id @GeneratedValue
    private Long id;
}
