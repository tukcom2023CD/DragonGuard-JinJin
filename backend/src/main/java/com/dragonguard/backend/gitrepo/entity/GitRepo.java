package com.dragonguard.backend.gitrepo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class GitRepo {
    @Id @GeneratedValue
    private Long id;

    private LocalDateTime githubUpdatedAt;
}
