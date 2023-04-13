package com.dragonguard.backend.member.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.issue.entity.Issue;
import com.dragonguard.backend.issue.service.IssueService;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.*;
import com.dragonguard.backend.member.entity.*;
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
        Member member = memberRepository.save(memberMapper.toEntity(githubId, role, authStep));
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

        if (!commits.isEmpty()) commits.forEach(member::addCommit);
        if (!pullRequests.isEmpty()) pullRequests.forEach(member::addPullRequest);
        if (!issues.isEmpty()) issues.forEach(member::addIssue);

        member.updateSumOfComments(contributions -
                (member.getSumOfCommits() + member.getSumOfPullRequests() + member.getSumOfIssues()));

        if (!isWalletAddressExist(member)) {
            updateTier(member);
            return;
        }
        setTransaction(member);
        updateTier(member);
    }

    @Transactional
    public void updateTier(Member member) {
        if (!isWalletAddressExist(member)) return;
        member.updateTier();
    }

    @Transactional
    public void updateContributions() {
        Member member = authService.getLoginUser();
        getCommitsByScraping(member.getGithubId());
        memberClientService.addMemberContribution(member);
        if (!isWalletAddressExist(member)) return;
        updateTier(member);
    }

    @Transactional
    public MemberResponse getMember() {
        Member member = authService.getLoginUser();
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
                    member.getSumOfComments(),
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
        Member member = getEntity(authService.getLoginUser().getId());
        member.updateWalletAddress(walletRequest.getWalletAddress());
        setTransaction(member);
    }

    public void setTransaction(Member member) {

        int commit = member.getSumOfCommits();
        int issue = member.getSumOfIssues();
        int pullRequest = member.getSumOfPullRequests();
        int comment = member.getSumOfComments();

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
        if (comment > 0) {
            blockchainService.setTransaction(
                    new ContractRequest(walletAddress,
                            ContributeType.COMMENT.toString(),
                            BigInteger.valueOf(comment),
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
        return memberMapper.toDetailResponse(memberResponse, gitOrganizations);
    }

    private void getCommitsByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }

    private boolean isWalletAddressExist(Member member) {
        return StringUtils.hasText(member.getWalletAddress());
    }
}
