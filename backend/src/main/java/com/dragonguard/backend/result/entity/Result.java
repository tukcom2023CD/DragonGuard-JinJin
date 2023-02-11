package com.dragonguard.backend.result.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@RedisHash(value = "result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result extends BaseTime implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Indexed
    private String searchId;

    @Builder
    public Result(String name, String searchId) {
        this.name = name;
        this.searchId = searchId;
    }
}
