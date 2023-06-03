package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.service.CommitService;
import com.dragonguard.backend.domain.contribution.service.ContributionService;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.service.IssueService;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.request.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.dto.request.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.domain.member.dto.response.MemberDetailResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.domain.member.dto.response.MemberResponse;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.entity.Tier;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.repository.OrganizationQueryRepository;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.service.PullRequestService;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.KafkaProducer;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버관련 서비스 로직을 담당하는 클래스
 */

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final MemberMapper memberMapper;
    private final CommitService commitService;
    private final ContributionService contributionService;
    private final BlockchainService blockchainService;
    private final AuthService authService;
    private final OrganizationQueryRepository organizationQueryRepository;
    private final PullRequestService pullRequestService;
    private final IssueService issueService;
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoRepository gitRepoRepository;
    private final KafkaProducer<KafkaContributionRequest> kafkaContributionProducer;
    private final KafkaProducer<KafkaRepositoryRequest> kafkaRepositoryProducer;

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
        kafkaContributionProducer.send(new KafkaContributionRequest(member.getGithubId()));
        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(member.getGithubId()));
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

        if (sumWithoutReviews > contributions) {
            member.deleteAllContributions();
            return;
        }

        member.updateSumOfReviews(contributions - sumWithoutReviews);

        if (!member.isWallAddressExist()) {
            updateTier(member);
            return;
        }
        transactionAndUpdateTier(member);
    }

    @Transactional
    public void updateTier(Member member) {
        if (!member.isWallAddressExist()) return;
        member.updateTier();
    }

    @Transactional
    public void updateContributions() {
        Member member = getLoginUserWithDatabase();
        getContributionSumByScraping(member.getGithubId());
        kafkaContributionProducer.send(new KafkaContributionRequest(member.getGithubId()));
        if (!member.isWallAddressExist()) return;
        transactionAndUpdateTier(member);
    }

    @Transactional
    public MemberResponse getMember() {
        Member member = getLoginUserWithDatabase();
        return getMemberResponse(member);
    }

    @Transactional
    public MemberResponse getMemberResponse(Member member) {
        UUID memberId = member.getId();
        Integer rank = memberQueryRepository.findRankingById(memberId);
        Long amount = member.getSumOfTokens();
        updateTier(member);
        Organization organization = member.getOrganization();

        if (organization == null) {
            return memberMapper.toResponse(
                    member,
                    rank,
                    amount);
        }

        Integer organizationRank = organizationQueryRepository.findRankingByMemberId(memberId);

        return memberMapper.toResponse(member, rank, amount, organization.getName(), organizationRank);
    }

    @Transactional
    public Member findMemberByGithubId(String githubId, AuthStep authStep) {
        return memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, authStep));
    }

    public List<MemberRankResponse> getMemberRanking(Pageable pageable) {
        return memberQueryRepository.findRanking(pageable);
    }

    @Transactional
    public void updateWalletAddress(WalletRequest walletRequest) {
        Member member = getLoginUserWithDatabase();
        member.updateWalletAddress(walletRequest.getWalletAddress());
    }

    @Transactional
    public void setTransaction(Member member) {

        Integer commit = member.getCommitSumWithRelation();
        Integer issue = member.getIssueSumWithRelation();
        Integer pullRequest = member.getPullRequestSumWithRelation();
        Optional<Integer> review = member.getSumOfReviews();

        String walletAddress = member.getWalletAddress();
        String githubId = member.getGithubId();

        if (commit <= 0) {
            return;
        }
        blockchainService.setTransaction(
                new ContractRequest(walletAddress,
                        ContributeType.COMMIT.toString(),
                        BigInteger.valueOf(commit),
                        githubId), member);

        if (issue <= 0) {
            return;
        }
        blockchainService.setTransaction(
                new ContractRequest(walletAddress,
                        ContributeType.ISSUE.toString(),
                        BigInteger.valueOf(issue),
                        githubId), member);

        if (pullRequest <= 0) {
            return;
        }
        blockchainService.setTransaction(
                new ContractRequest(walletAddress,
                        ContributeType.PULL_REQUEST.toString(),
                        BigInteger.valueOf(pullRequest),
                        githubId), member);

        review.ifPresent(
                rv -> {
                    if (rv <= 0) {
                        return;
                    }
                    blockchainService.setTransaction(
                            new ContractRequest(walletAddress,
                                    ContributeType.CODE_REVIEW.toString(),
                                    BigInteger.valueOf(rv),
                                    githubId), member);
                });
    }

    public List<MemberRankResponse> getMemberRankingByOrganization(Long organizationId, Pageable pageable) {
        return memberQueryRepository.findRankingByOrganization(organizationId, pageable);
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
        Member member = saveAndRequestClient(githubId, role, authStep);
        getContributionSumByScraping(githubId);
        return member;
    }

    @Transactional
    public MemberDetailResponse findMemberDetailByGithubId(String githubId) {
        Member member = memberRepository.findMemberByGithubId(githubId).orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, AuthStep.NONE));
        MemberResponse memberResponse = getMemberResponse(member);
        List<GitOrganization> gitOrganizations = gitOrganizationService.findGitOrganizationByGithubId(githubId);
        List<GitRepo> gitRepos = gitRepoRepository.findByGithubId(githubId);

        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(member.getGithubId()));
        kafkaContributionProducer.send(new KafkaContributionRequest(member.getGithubId()));

        return memberMapper.toDetailResponse(memberResponse, gitOrganizations, gitRepos);
    }

    @Transactional
    public void updateBlockchain() {
        transactionAndUpdateTier(getLoginUserWithDatabase());
    }

    @Transactional
    public void transactionAndUpdateTier(Member member) {
        setTransaction(member);
        updateTier(member);
    }

    public void getContributionSumByScraping(String githubId) {
        contributionService.scrapingCommits(githubId);
    }
}
