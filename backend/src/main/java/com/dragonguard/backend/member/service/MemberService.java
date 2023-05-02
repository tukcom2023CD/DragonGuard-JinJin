package com.dragonguard.backend.member.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.issue.entity.Issue;
import com.dragonguard.backend.issue.service.IssueService;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.*;
import com.dragonguard.backend.member.entity.*;
import com.dragonguard.backend.member.exception.IllegalContributionException;
import com.dragonguard.backend.member.mapper.MemberMapper;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.organization.entity.Organization;
import com.dragonguard.backend.organization.repository.OrganizationRepository;
import com.dragonguard.backend.pullrequest.entity.PullRequest;
import com.dragonguard.backend.pullrequest.service.PullRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버관련 서비스 로직을 담당하는 클래스
 */

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CommitService commitService;
    private final BlockchainService blockchainService;
    private final AuthService authService;
    private final OrganizationRepository organizationRepository;
    private final PullRequestService pullRequestService;
    private final IssueService issueService;
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoRepository gitRepoRepository;
    private final MemberClientService memberClientService;

    public Tier getTier() {
        return authService.getLoginUser().getTier();
    }

    @Transactional
    public IdResponse<UUID> saveMember(MemberRequest memberRequest, Role role) {
        return new IdResponse<>(scrapeAndGetSavedMember(memberRequest.getGithubId(), role, AuthStep.GITHUB_ONLY).getId());
    }

    @Transactional
    public Member saveAndRequestClient(String githubId, Role role, AuthStep authStep) {
        Member member = memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(githubId, role, authStep)));
        memberClientService.addMemberContribution(member);
        return member;
    }

    public Member saveAndGet(MemberRequest memberRequest, AuthStep authStep) {
        return memberRepository.findMemberByGithubId(memberRequest.getGithubId())
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(memberRequest, authStep)));
    }

    @Transactional
    public void addMemberCommitAndUpdate(String githubId, String name, String profileImage, Integer contributions) {
        Member member = findMemberByGithubId(githubId, AuthStep.NONE);
        member.updateNameAndImage(name, profileImage);

        List<Commit> commits = commitService.findCommits(githubId);
        List<PullRequest> pullRequests = pullRequestService.findPullrequestByGithubId(githubId);
        List<Issue> issues = issueService.findIssuesByGithubId(githubId);

        if (commits.isEmpty() || pullRequests.isEmpty() || issues.isEmpty()) return;

        commits.forEach(member::addCommit);
        pullRequests.forEach(member::addPullRequest);
        issues.forEach(member::addIssue);

        int sumWithoutReviews = member.getCommitSumWithRelation()
                + member.getPullRequestSumWithRelation()
                + member.getIssueSumWithRelation();

        if (sumWithoutReviews > contributions) throw new IllegalContributionException();

        member.updateSumOfReviews(contributions - sumWithoutReviews);

        if (!member.isWallAddressExist()) {
            updateTier(member);
            return;
        }
        setTransaction(member);
        updateTier(member);
    }

    @Transactional
    public void updateTier(Member member) {
        if (!member.isWallAddressExist()) return;
        member.updateTier();
    }

    @Transactional
    public void updateContributions() {
        Member member = getLoginUserWithDatabase();
        getCommitsByScraping(member.getGithubId());
        memberClientService.addMemberContribution(member);
        if (!member.isWallAddressExist()) return;
        setTransaction(member);
        updateTier(member);
    }

    @Transactional
    public MemberResponse getMember() {
        Member member = getLoginUserWithDatabase();
        return getMemberResponse(member);
    }

    @Transactional
    public MemberResponse getMemberResponse(Member member) {
        UUID memberId = member.getId();
        Integer rank = memberRepository.findRankingById(memberId);
        Long amount = member.getSumOfTokens();
        updateTier(member);
        OrganizationDetails organizationDetails = member.getOrganizationDetails();

        if (organizationDetails == null) {
            return memberMapper.toResponse(
                    member,
                    member.getSumOfIssues(),
                    member.getSumOfIssues(),
                    member.getSumOfPullRequests(),
                    member.getSumOfReviews(),
                    rank,
                    amount);
        }

        Organization organization = organizationRepository.findById(organizationDetails.getOrganizationId())
                .orElseThrow(EntityNotFoundException::new);

        Integer organizationRank = organizationRepository.findRankingByMemberId(memberId);

        return memberMapper.toResponse(member, member.getSumOfIssues(), rank, amount, organization.getName(), organizationRank);
    }

    @Transactional
    public Member findMemberByGithubId(String githubId, AuthStep authStep) {
        return memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, authStep));
    }

    public List<MemberRankResponse> getMemberRanking(Pageable pageable) {
        return memberRepository.findRanking(pageable);
    }

    @Transactional
    public void updateWalletAddress(WalletRequest walletRequest) {
        Member member = getLoginUserWithDatabase();
        member.updateWalletAddress(walletRequest.getWalletAddress());
    }

    public void setTransaction(Member member) {

        int commit = member.getCommitSumWithRelation();
        int issue = member.getIssueSumWithRelation();
        int pullRequest = member.getPullRequestSumWithRelation();
        int review = member.getSumOfReviews();

        String walletAddress = member.getWalletAddress();
        String githubId = member.getGithubId();

        if (commit > 0) {
            blockchainService.setTransaction(
                    new ContractRequest(walletAddress,
                            ContributeType.COMMIT.toString(),
                            BigInteger.valueOf(commit),
                            githubId), member);
        }
        if (issue > 0) {
            blockchainService.setTransaction(
                    new ContractRequest(walletAddress,
                            ContributeType.ISSUE.toString(),
                            BigInteger.valueOf(issue),
                            githubId), member);
        }
        if (pullRequest > 0) {
            blockchainService.setTransaction(
                    new ContractRequest(walletAddress,
                            ContributeType.PULL_REQUEST.toString(),
                            BigInteger.valueOf(pullRequest),
                            githubId), member);
        }
        if (review > 0) {
            blockchainService.setTransaction(
                    new ContractRequest(walletAddress,
                            ContributeType.CODE_REVIEW.toString(),
                            BigInteger.valueOf(review),
                            githubId), member);
        }
    }

    public List<MemberRankResponse> getMemberRankingByOrganization(Long organizationId, Pageable pageable) {
        return memberRepository.findRankingByOrganization(organizationId, pageable);
    }

    public Member getEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member getLoginUserWithDatabase() {
        return getEntity(authService.getLoginUser().getId());
    }

    @Transactional
    public Member scrapeAndGetSavedMember(String githubId, Role role, AuthStep authStep) {
        getCommitsByScraping(githubId);
        return saveAndRequestClient(githubId, role, authStep);
    }

    @Transactional
    public MemberDetailResponse findMemberDetailByGithubId(String githubId) {
        Member member = memberRepository.findMemberByGithubId(githubId).orElseThrow(EntityNotFoundException::new);
        MemberResponse memberResponse = getMemberResponse(member);
        List<GitOrganization> gitOrganizations = gitOrganizationService.findGitOrganizationByGithubId(githubId);
        List<GitRepo> gitRepos = gitRepoRepository.findByGithubId(githubId);
        return memberMapper.toDetailResponse(memberResponse, gitOrganizations, gitRepos);
    }

    @Transactional
    public void initMember() {
        memberRepository.findAll().stream()
                .map(member -> scrapeAndGetSavedMember(member.getGithubId(), Role.ROLE_ADMIN, AuthStep.GITHUB_ONLY))
                .forEach(m -> m.updateAuthStep(AuthStep.GITHUB_ONLY));
    }

    private void getCommitsByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }
}
