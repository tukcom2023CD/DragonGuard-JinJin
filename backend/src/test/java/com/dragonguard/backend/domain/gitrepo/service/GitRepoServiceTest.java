package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DatabaseTest
@DisplayName("GitRepo 서비스의")
class GitRepoServiceTest extends LoginTest {

    @Test
    @DisplayName("깃허브 레포지토리 API를 통해 받아오기")
    void findMembersByGitRepoWithClient() {
    }

    @Test
    @DisplayName("깃허브 레포지토리 기여자들을 비교를 위해 조회")
    void findMembersByGitRepoForCompare() {
    }

    @Test
    @DisplayName("깃허브 레포지토리의 두 기여자 조회")
    void findTwoGitRepoMember() {
    }

    @Test
    void findTwoGitRepos() {
    }

    @Test
    void updateClosedIssues() {
    }

    @Test
    void loadEntity() {
    }
}