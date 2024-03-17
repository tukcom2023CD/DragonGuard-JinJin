package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.member.dto.kafka.KafkaRepositoryRequest;
import com.dragonguard.backend.domain.member.dto.request.WalletRequest;
import com.dragonguard.backend.domain.member.dto.response.*;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;

import java.util.List;

@TransactionService
@RequiredArgsConstructor
public class MemberFacade {
    private final AuthService authService;
    private final MemberService memberService;
    private final MemberClientService memberClientService;
    private final KafkaProducer<KafkaRepositoryRequest> kafkaRepositoryProducer;

    public void updateContributions() {
        memberService.updateContributions();
    }

    public MemberResponse updateContributionsAndGetProfile() {
        return memberService.updateContributionsAndGetProfile();
    }

    public void updateBlockchain() {
        memberService.updateBlockchain();
    }

    public List<MemberRankResponse> findMemberRanking(final Pageable pageable) {
        return memberService.findMemberRanking(pageable);
    }

    public List<MemberRankResponse> findMemberRankingByOrganization(
            final Long organizationId, final Pageable pageable) {
        return memberService.findMemberRankingByOrganization(organizationId, pageable);
    }

    public void updateWalletAddress(final WalletRequest walletRequest) {
        memberService.updateWalletAddress(walletRequest);
    }

    public MemberDetailsResponse findMemberDetails(final String githubId) {
        return memberService.findMemberDetails(githubId);
    }

    public MemberGitReposAndGitOrganizationsResponse findMemberDetails() {
        return memberService.findMemberDetails();
    }

    public MemberGitOrganizationRepoResponse findMemberGitOrganizationRepo(
            final String organizationName) {
        sendRepositoryRequestToKafka(authService.getLoginUser().getGithubId());
        return new MemberGitOrganizationRepoResponse(
                memberService.getGitOrganizationByName(organizationName).getProfileImage(),
                memberClientService.requestGitOrganizationResponse(
                        authService.getLoginUser().getGithubToken(), organizationName));
    }

    private void sendRepositoryRequestToKafka(final String githubId) {
        final Member member = memberService.getMemberByGithubId(githubId);
        if (!memberService.isBlockchainUpdatable(member)) {
            return;
        }
        kafkaRepositoryProducer.send(new KafkaRepositoryRequest(githubId));
    }

    public MemberLoginVerifyResponse verifyMember() {
        return memberService.verifyMember();
    }

    public void withdraw() {
        memberService.withdraw();
    }

    public MemberResponse getMember() {
        return memberService.getMember();
    }
}
