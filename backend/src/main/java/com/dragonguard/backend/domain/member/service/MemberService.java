package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.History;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganizationMember;
import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaContributionRequest;
import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.*;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.mapper.MemberMapper;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.organization.service.OrganizationServiceImpl;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
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
    private final MemberClientService memberClientService;
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final OrganizationServiceImpl organizationService;
    private final GitOrganizationService gitOrganizationService;
    private final KafkaProducer<KafkaRepositoryRequest> kafkaRepositoryProducer;
    private final KafkaProducer<KafkaContributionRequest> kafkaContributionClientProducer;

    public Member findMemberOrSaveWithRole(final String githubId, final Role role, final AuthStep authStep) {
        if (memberRepository.existsByGithubId(githubId)) {
            return getMemberByGithubId(githubId);
        }
        return memberRepository.save(memberMapper.toEntity(githubId, role, authStep));
    }

    public Member saveIfNone(final String githubId, final AuthStep authStep, final String profileUrl) {
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

    private boolean isBlockchainUpdatable(final Member member) {
        return !member.getCommit().updatedCurrently() || member.getBlockchains().stream()
                .map(Blockchain::getHistories).flatMap(List::stream).allMatch(History::isUpdatable);
    }

    public MemberResponse getMember() {
        final Member member = authService.getLoginUser();
        return getMemberResponseWithValidateOrganization(member);
    }

    private MemberResponse getMemberResponseWithValidateOrganization(final Member member) {
        member.validateWalletAddressAndUpdateTier();
        if (hasNoOrganization(member)) {
            return memberMapper.toResponse(member, memberRepository.findRankingById(member.getId()));
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
    public List<MemberRankResponse> findMemberRanking(final Pageable pageable) {
        return memberRepository.findRanking(pageable);
    }

    public void updateWalletAddress(final WalletRequest walletRequest) {
        final Member member = authService.getLoginUser();
        member.updateWalletAddress(walletRequest.getWalletAddress());
    }

    private void sendContributionRequestToKafka(final String githubId) {
        Member member = getMemberByGithubId(githubId);
        if (!isBlockchainUpdatable(member)) {
            return;
        }
        kafkaContributionClientProducer.send(new KafkaContributionRequest(githubId));
    }

    private void sendRepositoryRequestToKafka(final String githubId) {
        final Member member = getMemberByGithubId(githubId);
        if (!isBlockchainUpdatable(member)) {
            return;
        }
        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(githubId));
    }

    @Transactional(readOnly = true)
    public List<MemberRankResponse> findMemberRankingByOrganization(final Long organizationId, final Pageable pageable) {
        return memberRepository.findRankingByOrganization(organizationId, pageable);
    }

    @Override
    public Member loadEntity(final UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
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

    public void updateContributionAndTransaction(final Member member) {
        memberClientService.addMemberContribution(member);
        member.validateWalletAddressAndUpdateTier();
    }

    private MemberGitReposAndGitOrganizationsResponse getMemberGitReposAndGitOrganizations(final Member member) {
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
        return member.getGitOrganizationMembers()
                .stream()
                .map(GitOrganizationMember::getGitOrganization)
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean hasNoOrganization(final Member member) {
        return Objects.isNull(member.getOrganization());
    }

    private void sendGitRepoAndContributionRequestToKafka(final String githubId) {
        sendContributionRequestToKafka(githubId);
        sendRepositoryRequestToKafka(githubId);
    }

    public MemberGitOrganizationRepoResponse findMemberGitOrganizationRepo(final String gitOrganizationName) {
        sendRepositoryRequestToKafka(authService.getLoginUser().getGithubId());
        return new MemberGitOrganizationRepoResponse(
                gitOrganizationService.getByName(gitOrganizationName).getProfileImage(),
                memberClientService.requestGitOrganizationResponse(authService.getLoginUser().getGithubToken(), gitOrganizationName));
    }

    public MemberDetailsResponse findMemberDetails(final String githubId) {
        final Member member = getMemberByGithubId(githubId);
        final Integer rank = memberRepository.findRankingById(member.getId());
        return memberMapper.toDetailsResponse(member, rank);
    }

    private Member getMemberByGithubId(final String githubId) {
        return memberRepository.findByGithubId(githubId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberResponse updateContributionsAndGetProfile() {
        final Member member = authService.getLoginUser();
        memberClientService.addMemberGitRepoAndGitOrganization(member);

        if (member.isWalletAddressExists()) {
            updateContributionAndTransaction(member);
        }
        return getMemberResponseWithValidateOrganization(member);
    }

    public MemberLoginVerifyResponse verifyMember() {
        final AuthStep authStep = authService.getLoginUser().getAuthStep();
        if (authStep.isGithubOnly()) {
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

    public WithdrawStatus withdraw() {
        authService.getLoginUser().withdraw();
        return WithdrawStatus.OK;
    }
}
