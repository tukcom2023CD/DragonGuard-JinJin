package com.dragonguard.backend.result.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author 김승진
 * @description 검색 결과를 담는 Redis Entity
 */

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(name, result.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
