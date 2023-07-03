package com.dragonguard.backend.domain.commit.service;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
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
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("커밋 내역 저장이 수행되는가")
    void saveCommits() {
        //given

        //when
        commitService.saveCommits(memberRepository.findById(loginUser.getId()).orElse(null), 100, LocalDateTime.now().getYear());
        List<Commit> commits = commitRepository.findAllByMember(loginUser);

        //then
        assertThat(commits).hasSize(1);
        assertThat(commits.get(0).getAmount()).isEqualTo(100);
        assertThat(commits.get(0).getMember().getGithubId()).isEqualTo(loginUser.getGithubId());
    }

    @Test
    @DisplayName("commit 단건 조회가 수행되는가")
    void loadEntity() {
        //given
        Commit given = commitRepository.save(Commit.builder().year(LocalDateTime.now().getYear()).amount(1).member(memberRepository.findById(loginUser.getId()).orElse(null)).build());

        //when
        Commit result = commitService.loadEntity(given.getId());

        //then
        assertThat(given).isEqualTo(result);
    }
}
