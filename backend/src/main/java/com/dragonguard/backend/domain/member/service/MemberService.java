package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.service.CommitService;
import com.dragonguard.backend.domain.contribution.dto.kafka.ContributionKafkaResponse;
import com.dragonguard.backend.domain.contribution.service.ContributionService;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.service.IssueService;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.*;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.entity.Tier;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.service.PullRequestService;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버관련 서비스 로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
    private final MemberRepository memberRepository;
    private final MemberClientService memberClientService;
    private final MemberMapper memberMapper;
    private final CommitService commitService;
    private final ContributionService contributionService;
    private final BlockchainService blockchainService;
    private final AuthService authService;
    private final OrganizationRepository organizationRepository;
    private final PullRequestService pullRequestService;
    private final IssueService issueService;
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoRepository gitRepoRepository;
    private final KafkaProducer<KafkaRepositoryRequest> kafkaRepositoryProducer;
    private final KafkaProducer<KafkaContributionRequest> kafkaContributionClientProducer;

    public Tier getTier() {
        Member member = getLoginUserWithPersistence();
        member.updateTier();
        return member.getTier();
    }

    public IdResponse<UUID> saveMember(final MemberRequest memberRequest, final Role role) {
        return new IdResponse<>(scrapeAndGetSavedMember(memberRequest.getGithubId(), role, AuthStep.GITHUB_ONLY).getId());
    }

    public Member saveAndRequestClient(final String githubId, final Role role, final AuthStep authStep) {
        Member member = findMemberOrSaveWithRole(githubId, role, authStep);
        getContributionSumByScraping(githubId);
        sendGitRepoRequestToKafka(githubId, member.getGithubToken());
        return member;
    }

    public Member findMemberOrSaveWithRole(final String githubId, final Role role, final AuthStep authStep) {
        if (memberRepository.existsByGithubId(githubId)) {
            return memberRepository.findByGithubId(githubId)
                    .orElseThrow(EntityNotFoundException::new);
        }
        Member member = memberRepository.save(memberMapper.toEntity(githubId, role, authStep));
        return loadEntity(member.getId());
    }

    public Member findMemberOrSave(final MemberRequest memberRequest, final AuthStep authStep, String profileUrl) {
        if (memberRepository.existsByGithubId(memberRequest.getGithubId())) {
            return memberRepository.findByGithubId(memberRequest.getGithubId())
                    .orElseThrow(EntityNotFoundException::new);
        }
        Member member = memberRepository.save(memberMapper.toEntity(memberRequest, authStep, profileUrl));
        return loadEntity(member.getId());
    }

    public void addMemberCommitAndUpdate(final ContributionKafkaResponse contributionKafkaResponse) {
        Member member = findMemberAndUpdate(contributionKafkaResponse);

        if (addContributionsIfNotEmpty(
                member,
                commitService.findCommitsByMember(member),
                pullRequestService.findPullRequestByMember(member),
                issueService.findIssuesByMember(member))) {
            return;
        }

        if (validateContributionAndUpdateReviews(contributionKafkaResponse, member)) return;

        sendTransactionIfWalletAddressExists(member);
    }

    public Member findMemberAndUpdate(final ContributionKafkaResponse contributionKafkaResponse) {
        Member member = findByGithubIdOrSaveWithAuthStep(contributionKafkaResponse.getGithubId(), AuthStep.NONE);
        member.updateNameAndImage(contributionKafkaResponse.getName(), contributionKafkaResponse.getProfileImage());
        return member;
    }

    public void sendTransactionIfWalletAddressExists(final Member member) {
        if (member.validateWalletAddressAndUpdateTier()) return;
        transactionAndUpdateTier(member);
    }

    public boolean validateContributionAndUpdateReviews(final ContributionKafkaResponse contributionKafkaResponse, final Member member) {
        int sumOfReviews = contributionKafkaResponse.getContribution() - getContributionSumWithoutReviews(member);
        if (member.validateContributionsAndDeleteIfInvalid(sumOfReviews)) return true;
        member.updateSumOfReviews(sumOfReviews);
        return false;
    }

    public boolean addContributionsIfNotEmpty(
            final Member member,
            final List<Commit> commits,
            final List<PullRequest> pullRequests,
            final List<Issue> issues) {
        if (isContributionEmpty(commits, pullRequests, issues)) return true;
        addContributions(member, commits, pullRequests, issues);
        return false;
    }

    public void addContributions(
            final Member member,
            final List<Commit> commits,
            final List<PullRequest> pullRequests,
            final List<Issue> issues) {

        commits.forEach(member::addCommit);
        pullRequests.forEach(member::addPullRequest);
        issues.forEach(member::addIssue);
    }

    public void updateContributions() {
        Member member = getLoginUserWithPersistence();
        getContributionSumByScraping(member.getGithubId());

        if (member.isWalletAddressExists()) transactionAndUpdateTier(member);
    }

    public MemberResponse getMember() {
        Member member = getLoginUserWithPersistence();
        return getMemberResponseWithValidateOrganization(member);
    }

    public MemberResponse getMemberResponseWithValidateOrganization(final Member member) {
        if (hasNoOrganization(member)) {
            return memberMapper.toResponse(member, memberRepository.findRankingById(member.getId()), member.getSumOfTokens());
        }
        return getMemberResponse(member);
    }

    public MemberResponse getMemberResponse(final Member member) {
        UUID memberId = member.getId();

        return memberMapper.toResponse(
                member,
                memberRepository.findRankingById(memberId),
                member.getSumOfTokens(),
                member.getOrganization().getName(),
                organizationRepository.findRankingByMemberId(memberId));
    }

    public Member findByGithubIdOrSaveWithAuthStep(final String githubId, final AuthStep authStep) {
        return memberRepository.findByGithubId(githubId)
                .orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, authStep));
    }

    @Transactional(readOnly = true)
    public List<MemberRankResponse> getMemberRanking(final Pageable pageable) {
        return memberRepository.findRanking(pageable);
    }

    public void updateWalletAddress(final WalletRequest walletRequest) {
        Member member = getLoginUserWithPersistence();
        member.updateWalletAddress(walletRequest.getWalletAddress());
        sendContributionRequestToKafka(member.getGithubId(), member.getGithubToken());
    }

    private void sendContributionRequestToKafka(final String githubId, final String githubToken) {
        kafkaContributionClientProducer.send(new KafkaContributionRequest(githubId, githubToken));
    }

    public void sendSmartContractTransaction(final Member member) {
        if (checkAndTransaction(member, member.getCommitSumWithRelation(), ContributeType.COMMIT)) return;

        if (checkAndTransaction(member, member.getIssueSumWithRelation(), ContributeType.ISSUE)) return;

        if (checkAndTransaction(member, member.getPullRequestSumWithRelation(), ContributeType.PULL_REQUEST)) return;

        member.getSumOfReviews().ifPresent(rv -> checkAndTransaction(member, rv, ContributeType.CODE_REVIEW));
    }

    public boolean checkAndTransaction(final Member member, final int contribution, final ContributeType contributeType) {
        if (contribution <= 0) return true;

        blockchainService.setTransaction(
                            new ContractRequest(
                                    contributeType.toString(),
                                    BigInteger.valueOf(contribution)),
                            member);

        return false;
    }

    @Transactional(readOnly = true)
    public List<MemberRankResponse> getMemberRankingByOrganization(final Long organizationId, final Pageable pageable) {
        return memberRepository.findRankingByOrganization(organizationId, pageable);
    }

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member getLoginUserWithPersistence() {
        return loadEntity(authService.getLoginUser().getId());
    }

    public Member scrapeAndGetSavedMember(final String githubId, final Role role, final AuthStep authStep) {
        Member member = saveAndRequestClient(githubId, role, authStep);
        getContributionSumByScraping(githubId);
        return member;
    }

    public MemberGitReposAndGitOrganizationsResponse findMemberDetails() {
        Member member = getLoginUserWithPersistence();
        String githubId = member.getGithubId();
        sendGitRepoAndContributionSumRequestToKafka(githubId, member.getGithubToken());

        return getMemberGitReposAndGitOrganizations(githubId, member);
    }

    public Member getMemberOrSaveAndScrape(final String githubId) {
        return memberRepository.findByGithubId(githubId).orElseGet(() -> scrapeAndGetSavedMember(githubId, Role.ROLE_USER, AuthStep.NONE));
    }

    public void updateBlockchain() {
        transactionAndUpdateTier(getLoginUserWithPersistence());
    }

    public void transactionAndUpdateTier(final Member member) {
        sendSmartContractTransaction(member);
        member.validateWalletAddressAndUpdateTier();
    }

    public void getContributionSumByScraping(final String githubId) {
        contributionService.scrapingCommits(githubId);
    }

    public MemberGitReposAndGitOrganizationsResponse getMemberGitReposAndGitOrganizations(final String githubId, final Member member) {
        return memberMapper.toRepoAndOrgResponse(
                member.getProfileImage(),
                gitOrganizationService.findGitOrganizationByMember(member),
                gitRepoRepository.findByGithubId(githubId));
    }

    public boolean hasNoOrganization(final Member member) {
        return member.getOrganization() == null;
    }

    public void sendGitRepoAndContributionSumRequestToKafka(final String githubId, final String githubToken) {
        sendGitRepoRequestToKafka(githubId, githubToken);
        getContributionSumByScraping(githubId);
    }

    private void sendGitRepoRequestToKafka(final String githubId, final String githubToken) {
        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(githubId, githubToken));
    }

    public boolean isContributionEmpty(final List<Commit> commits, final List<PullRequest> pullRequests, final List<Issue> issues) {
        return commits.isEmpty() || pullRequests.isEmpty() || issues.isEmpty();
    }

    public int getContributionSumWithoutReviews(final Member member) {
        return member.getCommitSumWithRelation()
                + member.getPullRequestSumWithRelation()
                + member.getIssueSumWithRelation();
    }

    public MemberGitOrganizationRepoResponse getMemberGitOrganizationRepo(String gitOrganizationName) {
        return new MemberGitOrganizationRepoResponse(
                gitOrganizationService.findByName(gitOrganizationName).getProfileImage(),
                memberClientService.requestGitOrganizationResponse(getLoginUserWithPersistence().getGithubToken(), gitOrganizationName));
    }

    public MemberDetailsResponse getMemberDetails(String githubId) {
        Member member = getMemberOrSaveAndScrape(githubId);
        Integer rank = memberRepository.findRankingById(member.getId());
        return memberMapper.toDetailsResponse(member, rank);
    }
}
