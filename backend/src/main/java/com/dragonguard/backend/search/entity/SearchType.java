package com.dragonguard.backend.search.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {
    REPOSITORY, ORGANIZATION, MEMBER
}
