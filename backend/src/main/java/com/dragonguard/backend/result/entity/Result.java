package com.dragonguard.backend.result.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@RedisHash(value = "result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result extends BaseTime implements Serializable {

    @Id
    private String id;

    private String name;

    @Indexed
    private String searchId;

    @Builder
    public Result(String name, String searchId) {
        this.name = name;
        this.searchId = searchId;
    }
}
