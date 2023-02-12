package com.dragonguard.backend.result.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@RedisHash("result")
public class Result implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Indexed
    private String searchId;

    @Builder
    public Result(String id, String name, String searchId) {
        this.id = id;
        this.name = name;
        this.searchId = searchId;
    }
}
