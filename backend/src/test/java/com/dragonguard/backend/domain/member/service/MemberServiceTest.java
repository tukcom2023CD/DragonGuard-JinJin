package com.dragonguard.backend.domain.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.repository.BlockchainRepository;
import com.dragonguard.backend.domain.blockchain.service.SmartContractService;
import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.codereview.repository.CodeReviewRepository;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.MemberGitReposAndGitOrganizationsResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Tier;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.support.database.LoginTest;
import com.dragonguard.backend.support.fixture.member.entity.MemberFixture;
import com.dragonguard.backend.support.fixture.organization.entity.OrganizationFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

@DisplayName("Member 서비스의")
class MemberServiceTest extends LoginTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CommitRepository commitRepository;
    @Autowired private IssueRepository issueRepository;
    @Autowired private PullRequestRepository pullRequestRepository;
    @Autowired private BlockchainRepository blockchainRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private CodeReviewRepository codeReviewRepository;
    @Autowired private EntityManager em;
    @MockBean private SmartContractService smartContractService;

    @Test
    @DisplayName("멤버 저장 기능이 수행되는가")
    void saveMember() {
        // given

        // when
        UUID userId =
                memberService.saveIfNone("HJ39", AuthStep.NONE, "http:/githubProfileUrl").getId();

        UUID adminResult = memberRepository.findById(loginUser.getId()).orElseThrow().getId();
        UUID userResult = memberRepository.findById(userId).orElseThrow().getId();

        // then
        assertThat(adminResult).isEqualTo(loginUser.getId());
        assertThat(userResult).isEqualTo(userId);
    }

    @Test
    @DisplayName("전체 활용도 업데이트 기능이 수행되는가")
    void updateContributions() {
        // given
        Integer issue = loginUser.getSumOfIssues();
        Integer commit = loginUser.getSumOfCommits();
        Integer pullRequest = loginUser.getSumOfPullRequests();
        Integer review = loginUser.getSumOfCodeReviews();

        // when
        memberService.updateContributions();

        // then
        assertThat(loginUser.getSumOfIssues()).isNotEqualTo(issue);
        assertThat(loginUser.getSumOfCommits()).isNotEqualTo(commit);
        assertThat(loginUser.getSumOfPullRequests()).isNotEqualTo(pullRequest);
        assertThat(loginUser.getSumOfCodeReviews()).isNotEqualTo(review);
    }

    @Test
    @DisplayName("블록체인 내역 업데이트가 수행되는가")
    void updateBlockchain() {
        // given
        int year = LocalDate.now().getYear();
        List<Blockchain> before = loginUser.getBlockchains();
        when(smartContractService.transfer(any(), any(), any())).thenReturn("123123");
        when(smartContractService.balanceOf(any())).thenReturn(BigInteger.valueOf(200L));

        em.flush();
        em.clear();

        loginUser = memberRepository.findById(loginUser.getId()).orElseThrow();

        commitRepository.save(Commit.builder().year(year).member(loginUser).amount(200).build());
        issueRepository.save(Issue.builder().year(year).member(loginUser).amount(200).build());
        pullRequestRepository.save(
                PullRequest.builder().year(year).member(loginUser).amount(200).build());
        codeReviewRepository.save(
                CodeReview.builder().year(year).member(loginUser).amount(200).build());

        loginUser = authService.getLoginUser();

        // when
        memberService.updateBlockchain();

        // then
        List<Blockchain> after = loginUser.getBlockchains();
        //
        // assertThat(before.stream().map(Blockchain::getAmount).mapToLong(BigInteger::longValue).sum())
        //
        // .isNotEqualTo(after.stream().map(Blockchain::getAmount).mapToLong(BigInteger::longValue).sum());
    }

    @Test
    @DisplayName("티어 조회가 수행되는가")
    void getTier() {
        // given
        loginUser = authService.getLoginUser();
        int contributionNum = 20000;
        Blockchain blockchain =
                blockchainRepository.save(
                        Blockchain.builder()
                                .member(loginUser)
                                .contributeType(ContributeType.COMMIT)
                                .build());
        blockchain.addHistory(BigInteger.valueOf(20000L), "woiufheawoiuerhg");

        loginUser.updateTier();

        em.flush();
        em.clear();

        // when
        Tier tier = authService.getLoginUser().getTier();

        // then
        assertThat(tier).isEqualTo(loginUser.checkTier(contributionNum));
    }

    @Test
    @DisplayName("조직별 멤버 랭킹 리스트 조회가 수행되는가")
    void getMemberRankingByOrganization() {
        // given
        Organization org = organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());
        Member member1 = authService.getLoginUser();
        Member member2 = memberRepository.save(MemberFixture.POSITE.toEntity());
        Member member3 = memberRepository.save(MemberFixture.SAMMUELWOOJAE.toEntity());
        Member member4 = memberRepository.save(MemberFixture.HJ39.toEntity());

        org.addMember(member1, "a@tukorea.ac.kr");
        org.addMember(member2, "b@tukorea.ac.kr");
        org.addMember(member3, "c@tukorea.ac.kr");
        org.addMember(member4, "d@tukorea.ac.kr");

        // when
        List<MemberRankResponse> response =
                memberService.findMemberRankingByOrganization(org.getId(), PageRequest.of(0, 4));

        // then
        assertThat(response.stream().map(MemberRankResponse::getId).collect(Collectors.toList()))
                .isEqualTo(
                        List.of(
                                member1.getId(),
                                member2.getId(),
                                member3.getId(),
                                member4.getId()));
    }

    @Test
    @DisplayName("지갑 주소 갱신이 수행되는가")
    void updateWalletAddress() {
        // given
        Member member = authService.getLoginUser();
        String before = member.getWalletAddress();

        // when
        String after = "Dragon1234Guard4321JinJin";
        memberService.updateWalletAddress(new WalletRequest(after));

        em.flush();
        em.clear();

        String walletAddress =
                memberRepository.findById(loginUser.getId()).get().getWalletAddress();

        // then
        assertThat(before).isNotEqualTo(after);
        assertThat(walletAddress).isEqualTo(after);
    }

    @Test
    @DisplayName("멤버 본인 상세 조회가 수행되는가")
    void getMember() {
        // given
        Organization org = organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());
        Member member = authService.getLoginUser();
        org.addMember(member, "ohksj77@tukorea.ac.kr");

        // when
        MemberResponse result = memberService.getMember();

        // then
        assertThat(result.getId()).isEqualTo(loginUser.getId());
    }

    @Test
    @DisplayName("멤버 레포와 깃 org 조회가 수행되는가")
    void findMemberDetailByGithubId() {
        // given
        Member member = authService.getLoginUser();
        Organization org = organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());
        org.addMember(member, "ohksj77@tukorea.ac.kr");
        member.finishAuth(org);

        // when
        MemberGitReposAndGitOrganizationsResponse result = memberService.findMemberDetails();

        // then
        assertThat(result.getGitOrganizations()).isNotNull(); // todo 더 정확한 validation 필요
    }
}
