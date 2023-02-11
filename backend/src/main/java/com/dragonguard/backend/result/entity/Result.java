package com.dragonguard.backend.result.entity;

import com.dragonguard.backend.global.BaseTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Builder
@RedisHash(value = "result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Result extends BaseTime implements Serializable {

    @Id
    private String id;

    private String name;

    @Indexed
    private String searchId;
}
