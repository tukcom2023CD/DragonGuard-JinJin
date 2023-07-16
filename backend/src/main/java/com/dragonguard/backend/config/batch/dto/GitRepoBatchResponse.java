package com.dragonguard.backend.config.batch.dto;

import com.dragonguard.backend.domain.gitrepo.dto.client.Author;
import com.dragonguard.backend.domain.gitrepo.dto.client.Week;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoBatchResponse {
    private Integer total;
    private Week[] weeks;
    private Author author;
}
