package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoContribution;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import com.dragonguard.backend.support.fixture.member.entity.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DatabaseTest
@DisplayName("GitRepo 서비스의")
class GitRepoServiceTest extends LoginTest {
    @Autowired private GitRepoService gitRepoService;
    @Autowired private GitRepoRepository gitRepoRepository;
    @Autowired private GitRepoMemberRepository gitRepoMemberRepository;
    @MockBean private GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    @MockBean private GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;
    @MockBean private KafkaProducer<GitRepoNameRequest> kafkaIssueProducer;

    @Test
    @DisplayName("깃허브 레포지토리 두개 비교를 위해 한 번에 조회가 수행되는가")
    void findTwoGitRepos() {
        //given
        GitRepo gitRepo1 = GitRepo.builder().name("ohksj77/Algorithm_java").build();
        GitRepo gitRepo2 = GitRepo.builder().name("ohksj77/Algorithm_py").build();

        Member newMember = MemberFixture.HJ39.toEntity();
        memberRepository.save(newMember);

        GitRepoMember gitRepoMember1 = GitRepoMember.builder()
                .member(loginUser)
                .gitRepo(gitRepo1)
                .gitRepoContribution(new GitRepoContribution(1, 2, 3))
                .build();
        GitRepoMember gitRepoMember2 = GitRepoMember.builder()
                .member(newMember)
                .gitRepo(gitRepo2)
                .gitRepoContribution(new GitRepoContribution(4, 5, 6))
                .build();

        gitRepoRepository.saveAll(List.of(gitRepo1, gitRepo2));
        gitRepoMemberRepository.saveAll(List.of(gitRepoMember1, gitRepoMember2));

        GitRepoCompareRequest request = new GitRepoCompareRequest(gitRepo1.getName(), gitRepo2.getName());
        GitRepoClientResponse expected = new GitRepoClientResponse(gitRepo1.getName(), 1, 2, 3, 4, 5, 6);

        when(gitRepoClient.requestToGithub(any())).thenReturn(expected);
        doNothing().when(kafkaIssueProducer).send(any());
        when(gitRepoLanguageClient.requestToGithub(any())).thenReturn(Map.of("Java", 10000));

        //when
        TwoGitRepoResponse result = gitRepoService.findTwoGitReposAndUpdate(request);

        //then
        assertThat(result.getFirstRepo().getGitRepo()).isEqualTo(expected);
        assertThat(result.getSecondRepo().getLanguages()).containsEntry("Java", 10000);
    }

    @Test
    @DisplayName("깃허브 레포지토리 id로 조회가 수행되는가")
    void loadEntity() {
        //given
        GitRepo given = gitRepoRepository.save(GitRepo.builder().name("tukcom2023CD/DragonGuard").build());

        //when
        GitRepo result = gitRepoService.loadEntity(given.getId());

        //then
        assertThat(result).isEqualTo(given);
    }
}
