package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.contribution.dto.response.ContributionScrapingResponse;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("commit 서비스의")
class CommitServiceTest extends LoginTest {
    @Autowired private CommitService commitService;
    @Autowired private CommitRepository commitRepository;

    @Test
    @DisplayName("커밋 내역 저장")
    void saveCommits() {
        //given
        ContributionScrapingResponse given = new ContributionScrapingResponse(loginUser.getGithubId(), 100);

        //when
        commitService.saveCommits(given, memberRepository.findById(loginUser.getId()).orElse(null));
        List<Commit> commits = commitRepository.findAllByMember(loginUser);

        //then
        assertThat(commits).hasSize(1);
        assertThat(commits.get(0).getAmount()).isEqualTo(100);
        assertThat(commits.get(0).getMember().getGithubId()).isEqualTo(loginUser.getGithubId());
    }

    @Test
    @DisplayName("커밋 내역 리스트 조회")
    void findCommits() {
        //given
        commitRepository.save(Commit.builder().year(LocalDateTime.now().getYear()).amount(1).member(memberRepository.findById(loginUser.getId()).orElse(null)).build());
        commitRepository.save(Commit.builder().year(LocalDateTime.now().getYear()).amount(2).member(memberRepository.findById(loginUser.getId()).orElse(null)).build());

        //when
        List<Commit> commits = commitService.findCommitsByMember(loginUser);

        //then
        assertThat(commits).hasSize(2);
        assertThat(commits.get(0).getAmount()).isEqualTo(1);
        assertThat(commits.get(1).getAmount()).isEqualTo(2);
        assertThat(commits.get(0).getMember().getGithubId()).isEqualTo(commits.get(1).getMember().getGithubId()).isEqualTo(loginUser.getGithubId());
    }

    @Test
    @DisplayName("commit 단건 조회")
    void loadEntity() {
        //given
        Commit given = commitRepository.save(Commit.builder().year(LocalDateTime.now().getYear()).amount(1).member(memberRepository.findById(loginUser.getId()).orElse(null)).build());

        //when
        Commit result = commitService.loadEntity(given.getId());

        //then
        assertThat(given).isEqualTo(result);
    }
}
