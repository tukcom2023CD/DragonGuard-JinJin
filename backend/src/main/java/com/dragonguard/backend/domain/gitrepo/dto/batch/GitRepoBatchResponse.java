package com.dragonguard.backend.domain.gitrepo.dto.batch;

import com.dragonguard.backend.domain.gitrepomember.dto.client.Author;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
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
