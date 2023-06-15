package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.client.Author;
import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
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

import java.util.Arrays;
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
    @MockBean private GithubClient<GitRepoRequest, GitRepoMemberClientResponse[]> gitRepoMemberClient;
    @MockBean private GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    @MockBean private GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;
    @MockBean private KafkaProducer<GitRepoNameRequest> kafkaIssueProducer;

    @Test
    @DisplayName("깃허브 레포지토리 API를 통해 받아오기가 수행되는가")
    void findMembersByGitRepoWithClient() {
        //given
        GitRepoRequest request = new GitRepoRequest("tukcom2023CD/DragonGuard-JinJin", 2023);
        GitRepoMemberClientResponse[] expected = {
                new GitRepoMemberClientResponse(1000,
                new Week[]{new Week(100, 50, 100)},
                new Author(loginUser.getGithubId(), loginUser.getProfileImage()))};
        List<GitRepoMemberClientResponse> contributions = Arrays.asList(expected);

        when(gitRepoMemberClient.requestToGithub(any())).thenReturn(expected);

        //when
        List<GitRepoMemberResponse> result = gitRepoService.findMembersByGitRepoWithClient(request);

        //then
        GitRepoMemberResponse response = result.get(0);
        GitRepoMemberClientResponse clientResponse = contributions.get(0);

        assertThat(response.getAdditions()).isEqualTo(clientResponse.getWeeks()[0].getA());
        assertThat(response.getDeletions()).isEqualTo(clientResponse.getWeeks()[0].getD());
        assertThat(response.getCommits()).isEqualTo(clientResponse.getTotal());
        assertThat(response.getGithubId()).isEqualTo(loginUser.getGithubId());
    }

    @Test
    @DisplayName("깃허브 레포지토리 기여자들을 비교를 위해 조회가 수행되는가")
    void findMembersByGitRepoForCompare() {
        //given
        GitRepoCompareRequest request = new GitRepoCompareRequest("ohksj77/Algorithm_java", "ohksj77/Algorithm_py");
        GitRepoMemberClientResponse[] expected = {
                new GitRepoMemberClientResponse(1000,
                        new Week[]{new Week(100, 50, 100)},
                        new Author(loginUser.getGithubId(), loginUser.getProfileImage()))};
        List<GitRepoMemberClientResponse> contributions = Arrays.asList(expected);

        when(gitRepoMemberClient.requestToGithub(any())).thenReturn(expected);

        //when
        TwoGitRepoMemberResponse result = gitRepoService.findMembersByGitRepoForCompare(request);

        //then
        GitRepoMemberResponse firstResponse = result.getFirstResult().get(0);
        GitRepoMemberResponse secondResponse = result.getSecondResult().get(0);
        GitRepoMemberClientResponse clientResponse = contributions.get(0);

        assertThat(firstResponse.getGithubId()).isEqualTo(loginUser.getGithubId());
        assertThat(firstResponse.getAdditions()).isEqualTo(clientResponse.getWeeks()[0].getA());
        assertThat(firstResponse.getDeletions()).isEqualTo(clientResponse.getWeeks()[0].getD());
        assertThat(firstResponse.getCommits()).isEqualTo(clientResponse.getTotal());

        assertThat(secondResponse.getGithubId()).isEqualTo(loginUser.getGithubId());
        assertThat(secondResponse.getAdditions()).isEqualTo(clientResponse.getWeeks()[0].getA());
        assertThat(secondResponse.getDeletions()).isEqualTo(clientResponse.getWeeks()[0].getD());
        assertThat(secondResponse.getCommits()).isEqualTo(clientResponse.getTotal());
    }

    @Test
    @DisplayName("깃허브 레포지토리의 두 기여자 조회가 수행되는가")
    void findTwoGitRepoMember() {
        //given
        doNothing().when(kafkaIssueProducer).send(any());

        GitRepo gitRepo = GitRepo.builder().name("tukcom2023CD/DragonGuard-JinJin").build();
        Member newMember = MemberFixture.HJ39.toEntity();
        memberRepository.save(newMember);

        GitRepoMember ohksj77 = GitRepoMember.builder()
                .member(loginUser)
                .gitRepo(gitRepo)
                .gitRepoContribution(new GitRepoContribution(1, 2, 3))
                .build();
        GitRepoMember HJ39 = GitRepoMember.builder()
                .member(newMember)
                .gitRepo(gitRepo)
                .gitRepoContribution(new GitRepoContribution(4, 5, 6))
                .build();
        gitRepoRepository.save(gitRepo);
        gitRepoMemberRepository.saveAll(List.of(ohksj77, HJ39));

        //when
        GitRepoMemberCompareResponse result = gitRepoService.findTwoGitRepoMember(new GitRepoMemberCompareRequest(ohksj77.getMember().getGithubId(), gitRepo.getName(), HJ39.getMember().getGithubId(), gitRepo.getName()));

        //then
        GitRepoMemberResponse firstMember = result.getFirstMember();
        GitRepoMemberResponse secondMember = result.getSecondMember();

        GitRepoContribution firstMemberContribution = ohksj77.getGitRepoContribution();
        GitRepoContribution secondMemberContribution = HJ39.getGitRepoContribution();

        assertThat(firstMember.getGithubId()).isEqualTo(ohksj77.getMember().getGithubId());
        assertThat(firstMember.getCommits()).isEqualTo(firstMemberContribution.getCommits());
        assertThat(firstMember.getAdditions()).isEqualTo(firstMemberContribution.getAdditions());
        assertThat(firstMember.getDeletions()).isEqualTo(firstMemberContribution.getDeletions());

        assertThat(secondMember.getGithubId()).isEqualTo(HJ39.getMember().getGithubId());
        assertThat(secondMember.getCommits()).isEqualTo(secondMemberContribution.getCommits());
        assertThat(secondMember.getAdditions()).isEqualTo(secondMemberContribution.getAdditions());
        assertThat(secondMember.getDeletions()).isEqualTo(secondMemberContribution.getDeletions());
    }

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
        TwoGitRepoResponse result = gitRepoService.findTwoGitRepos(request);

        //then
        assertThat(result.getFirstRepo().getGitRepo()).isEqualTo(expected);
        assertThat(result.getSecondRepo().getLanguages()).containsEntry("Java", 10000);
    }

    @Test
    @DisplayName("깃허브 레포지토리 id로 조회가 수행되는가")
    void loadEntity() {
        //given
        GitRepo given = gitRepoRepository.save(GitRepo.builder().name("tukcom2023CD/DragonGuard-JinJin").build());

        //when
        GitRepo result = gitRepoService.loadEntity(given.getId());

        //then
        assertThat(result).isEqualTo(given);
    }
}
