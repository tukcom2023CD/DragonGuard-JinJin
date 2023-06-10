package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.service.CommitService;
import com.dragonguard.backend.domain.contribution.dto.kafka.ContributionKafkaResponse;
import com.dragonguard.backend.domain.contribution.service.ContributionService;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.service.IssueService;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
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
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버관련 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
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

    public IdResponse<UUID> saveMember(MemberRequest memberRequest, Role role) {
        return new IdResponse<>(scrapeAndGetSavedMember(memberRequest.getGithubId(), role, AuthStep.GITHUB_ONLY).getId());
    }

    public Member saveAndRequestClient(String githubId, Role role, AuthStep authStep) {
        Member member = memberRepository.findByGithubId(githubId)
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(githubId, role, authStep)));
        kafkaContributionProducer.send(new KafkaContributionRequest(member.getGithubId()));
        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(member.getGithubId()));
        return member;
    }

    public Member saveAndGet(MemberRequest memberRequest, AuthStep authStep) {
        return memberRepository.findByGithubId(memberRequest.getGithubId())
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(memberRequest, authStep)));
    }

    public void addMemberCommitAndUpdate(ContributionKafkaResponse contributionKafkaResponse) {
        String githubId = contributionKafkaResponse.getGithubId();
        Member member = findMemberByGithubId(githubId, AuthStep.NONE);
        member.updateNameAndImage(contributionKafkaResponse.getName(), contributionKafkaResponse.getProfileImage());

        List<Commit> commits = commitService.findCommits(githubId);
        List<PullRequest> pullRequests = pullRequestService.findPullRequestByGithubId(githubId);
        List<Issue> issues = issueService.findIssuesByGithubId(githubId);

        if (commits.isEmpty() || pullRequests.isEmpty() || issues.isEmpty()) return;

        commits.forEach(member::addCommit);
        pullRequests.forEach(member::addPullRequest);
        issues.forEach(member::addIssue);

        int sumWithoutReviews = member.getCommitSumWithRelation()
                + member.getPullRequestSumWithRelation()
                + member.getIssueSumWithRelation();

        Integer contributions = contributionKafkaResponse.getContribution();

        if (sumWithoutReviews > contributions) {
            member.deleteAllContributions();
            return;
        }

        member.updateSumOfReviews(contributions - sumWithoutReviews);

        if (!member.isWalletAddressExist()) {
            updateTier(member);
            return;
        }
        transactionAndUpdateTier(member);
    }

    public void updateTier(Member member) {
        if (!member.isWalletAddressExist()) return;
        member.updateTier();
    }

    public void updateContributions() {
        Member member = getLoginUserWithPersistence();
        getContributionSumByScraping(member.getGithubId());
        kafkaContributionProducer.send(new KafkaContributionRequest(member.getGithubId()));
        if (!member.isWalletAddressExist()) return;
        transactionAndUpdateTier(member);
    }

    public MemberResponse getMember() {
        Member member = getLoginUserWithPersistence();
        return getMemberResponse(member);
    }

    public MemberResponse getMemberResponse(Member member) {
        UUID memberId = member.getId();
        Integer rank = memberQueryRepository.findRankingById(memberId);
        Long amount = member.getSumOfTokens();
        updateTier(member);
        Organization organization = member.getOrganization();

        if (organization == null) return memberMapper.toResponse(member, rank, amount);

        Integer organizationRank = organizationQueryRepository.findRankingByMemberId(memberId);

        return memberMapper.toResponse(member, rank, amount, organization.getName(), organizationRank);
    }

    public Member findMemberByGithubId(String githubId, AuthStep authStep) {
        return memberRepository.findByGithubId(githubId)
                .orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, authStep));
    }

    @Transactional(readOnly = true)
    public List<MemberRankResponse> getMemberRanking(Pageable pageable) {
        return memberQueryRepository.findRanking(pageable);
    }

    public void updateWalletAddress(WalletRequest walletRequest) {
        Member member = getLoginUserWithPersistence();
        member.updateWalletAddress(walletRequest.getWalletAddress());
    }

    public void setTransaction(Member member) {

        int commit = member.getCommitSumWithRelation();
        int issue = member.getIssueSumWithRelation();
        int pullRequest = member.getPullRequestSumWithRelation();
        Optional<Integer> review = member.getSumOfReviews();

        if (checkAndTransaction(member, commit, ContributeType.COMMIT)) return;
        if (checkAndTransaction(member, issue, ContributeType.ISSUE)) return;
        if (checkAndTransaction(member, pullRequest, ContributeType.PULL_REQUEST)) return;
        review.ifPresent(rv -> checkAndTransaction(member, rv, ContributeType.CODE_REVIEW));
    }

    public boolean checkAndTransaction(Member member, int commit, ContributeType contributeType) {
        if (commit <= 0) return true;

        blockchainService.setTransaction(
                new ContractRequest(
                        contributeType.toString(),
                        BigInteger.valueOf(commit)), member);

        return false;
    }

    @Transactional(readOnly = true)
    public List<MemberRankResponse> getMemberRankingByOrganization(Long organizationId, Pageable pageable) {
        return memberQueryRepository.findRankingByOrganization(organizationId, pageable);
    }

    @Override
    public Member loadEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member getLoginUserWithPersistence() {
        return loadEntity(authService.getLoginUser().getId());
    }

    public Member scrapeAndGetSavedMember(String githubId, Role role, AuthStep authStep) {
        Member member = saveAndRequestClient(githubId, role, authStep);
        getContributionSumByScraping(githubId);
        return member;
    }

    public MemberDetailResponse findMemberDetailByGithubId(String githubId) {
        Member member = memberRepository.findByGithubId(githubId).orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, AuthStep.NONE));
        MemberResponse memberResponse = getMemberResponse(member);
        List<GitOrganization> gitOrganizations = gitOrganizationService.findGitOrganizationByGithubId(githubId);
        List<GitRepo> gitRepos = gitRepoRepository.findByGithubId(githubId);

        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(member.getGithubId()));
        kafkaContributionProducer.send(new KafkaContributionRequest(member.getGithubId()));

        return memberMapper.toDetailResponse(memberResponse, gitOrganizations, gitRepos);
    }

    public void updateBlockchain() {
        transactionAndUpdateTier(getLoginUserWithPersistence());
    }

    public void transactionAndUpdateTier(Member member) {
        setTransaction(member);
        updateTier(member);
    }

    public void getContributionSumByScraping(String githubId) {
        contributionService.scrapingCommits(githubId);
    }
}
