package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.History;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganizationMember;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.dto.kafka.ContributionEvent;
import com.dragonguard.backend.domain.member.dto.kafka.RepositoryEvent;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.*;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.organization.service.OrganizationService;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.EventProducer;
import com.dragonguard.backend.global.template.service.EntityLoader;
import com.dragonguard.backend.utils.RedisRankingUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버관련 서비스 로직을 담당하는 클래스
 */
@TransactionService
@RequiredArgsConstructor
public class MemberService implements EntityLoader<Member, UUID> {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final OrganizationService organizationService;
    private final GitOrganizationService gitOrganizationService;
    private final EventProducer<RepositoryEvent> kafkaRepositoryProducer;
    private final EventProducer<ContributionEvent> kafkaContributionClientProducer;
    private final RedisRankingUtils redisRankingUtils;

    public Member findMemberOrSaveWithRole(
            final String githubId, final Role role, final AuthStep authStep) {
        if (memberRepository.existsByGithubId(githubId)) {
            return getMemberByGithubId(githubId);
        }
        return memberRepository.save(memberMapper.toEntity(githubId, role, authStep));
    }

    public Member saveIfNone(
            final String githubId, final AuthStep authStep, final String profileUrl) {
        if (memberRepository.existsByGithubId(githubId)) {
            return getMemberByGithubId(githubId);
        }
        return memberRepository.save(memberMapper.toEntity(githubId, authStep, profileUrl));
    }

    public void updateContributions() {
        final Member member = authService.getLoginUser();
        if (isBlockchainUpdatable(member)) {
            sendGitRepoAndContributionRequestToKafka(member.getGithubId());
        }
    }

    public boolean isBlockchainUpdatable(final Member member) {
        final List<Blockchain> blockchains = member.getBlockchains();
        if (blockchains.isEmpty()) {
            return true;
        }
        return blockchains.stream()
                .map(Blockchain::getHistories)
                .flatMap(List::stream)
                .allMatch(History::isUpdatable);
    }

    public MemberResponse getMember() {
        final Member member = authService.getLoginUser();
        return getMemberResponseWithValidateOrganization(member);
    }

    private MemberResponse getMemberResponseWithValidateOrganization(final Member member) {
        member.validateWalletAddressAndUpdateTier();
        if (hasNoOrganization(member)) {
            return memberMapper.toResponse(
                    member, memberRepository.findRankingById(member.getId()));
        }
        return getMemberResponse(member);
    }

    private MemberResponse getMemberResponse(final Member member) {
        final UUID memberId = member.getId();

        return memberMapper.toResponse(
                member,
                memberRepository.findRankingById(memberId),
                member.getOrganization().getName(),
                organizationService.findRankingByMemberId(memberId, member.getGithubId()));
    }

    @Transactional(readOnly = true)
    public MemberRankResponses findMemberRanking(final Pageable pageable) {
        final List<MemberRankResponse> ranking = memberRepository.findRanking(pageable);
        return new MemberRankResponses(ranking);
    }

    public void updateWalletAddress(final WalletRequest walletRequest) {
        final Member member = authService.getLoginUser();
        member.updateWalletAddress(walletRequest.getWalletAddress());
    }

    private void sendContributionRequestToKafka(final String githubId) {
        final Member member = getMemberByGithubId(githubId);
        if (!isBlockchainUpdatable(member)) {
            return;
        }
        kafkaContributionClientProducer.send(new ContributionEvent(githubId));
    }

    private void sendRepositoryRequestToKafka(final String githubId) {
        final Member member = getMemberByGithubId(githubId);
        if (!isBlockchainUpdatable(member)) {
            return;
        }
        kafkaRepositoryProducer.send(new RepositoryEvent(githubId));
    }

    @Transactional(readOnly = true)
    public MemberRankResponses findMemberRankingByOrganization(
            final Long organizationId, final Pageable pageable) {
        final List<MemberRankResponse> rankingByOrganization =
                memberRepository.findRankingByOrganization(organizationId, pageable);
        return new MemberRankResponses(rankingByOrganization);
    }

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public MemberGitReposAndGitOrganizationsResponse findMemberDetails() {
        final Member member = authService.getLoginUser();
        sendRepositoryRequestToKafka(member.getGithubId());

        return getMemberGitReposAndGitOrganizations(member);
    }

    public void updateBlockchain() {
        updateTier(authService.getLoginUser());
    }

    private void updateTier(final Member member) {
        member.validateWalletAddressAndUpdateTier();
    }

    private MemberGitReposAndGitOrganizationsResponse getMemberGitReposAndGitOrganizations(
            final Member member) {
        return memberMapper.toRepoAndOrgResponse(
                member.getProfileImage(),
                findMemberGitOrganization(member),
                findMemberGitRepo(member));
    }

    private List<GitRepo> findMemberGitRepo(final Member member) {
        return member.getGitRepoMembers().stream()
                .map(GitRepoMember::getGitRepo)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<GitOrganization> findMemberGitOrganization(final Member member) {
        return member.getGitOrganizationMembers().stream()
                .map(GitOrganizationMember::getGitOrganization)
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean hasNoOrganization(final Member member) {
        return Objects.isNull(member.getOrganization());
    }

    public void sendGitRepoAndContributionRequestToKafka(final String githubId) {
        sendContributionRequestToKafka(githubId);
        sendRepositoryRequestToKafka(githubId);
    }

    public MemberDetailsResponse findMemberDetails(final String githubId) {
        final Member member = getMemberByGithubId(githubId);
        final Integer rank = memberRepository.findRankingById(member.getId());
        return memberMapper.toDetailsResponse(member, rank);
    }

    public Member getMemberByGithubId(final String githubId) {
        return memberRepository.findByGithubId(githubId).orElseThrow(EntityNotFoundException::new);
    }

    public MemberResponse updateContributionsAndGetProfile() {
        final Member member = authService.getLoginUser();
        kafkaRepositoryProducer.send(new RepositoryEvent(member.getGithubId()));

        sendContributionRequestToKafka(member.getGithubId());
        return getMemberResponseWithValidateOrganization(member);
    }

    public MemberLoginVerifyResponse verifyMember() {
        final AuthStep authStep = authService.getLoginUser().getAuthStep();
        if (authStep.isNone()) {
            return new MemberLoginVerifyResponse(false);
        }
        return new MemberLoginVerifyResponse(true);
    }

    public Boolean isServiceMember(final String githubId) {
        if (!memberRepository.existsByGithubId(githubId)) {
            return false;
        }
        return getMemberByGithubId(githubId).isServiceMember();
    }

    public void withdraw() {
        authService.getLoginUser().withdraw();
    }

    public GitOrganization getGitOrganizationByName(final String gitOrganizationName) {
        return gitOrganizationService.getByName(gitOrganizationName);
    }
}
