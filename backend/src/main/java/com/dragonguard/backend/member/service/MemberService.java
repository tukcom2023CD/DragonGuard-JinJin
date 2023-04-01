package com.dragonguard.backend.member.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.*;
import com.dragonguard.backend.member.mapper.MemberMapper;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.organization.entity.Organization;
import com.dragonguard.backend.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final MemberMapper memberMapper;
    private final CommitService commitService;
    private final BlockchainService blockchainService;
    private final AuthService authService;
    private final OrganizationRepository organizationRepository;

    public Tier getTier() {
        return authService.getLoginUser().getTier();
    }

    public IdResponse<UUID> saveMember(MemberRequest memberRequest, Role role) {
        return new IdResponse<>(scrapeAndGetSavedMember(memberRequest.getGithubId(), role, AuthStep.GITHUB_ONLY).getId());
    }

    public Member saveAndGet(String githubId, Role role, AuthStep authStep) {
        return memberRepository.save(memberMapper.toEntity(githubId, role, authStep));
    }

    public Member saveAndGet(MemberRequest memberRequest, AuthStep authStep) {
        return memberRepository.findMemberByGithubId(memberRequest.getGithubId())
                .orElseGet(() -> memberRepository.save(memberMapper.toEntity(memberRequest, authStep)));
    }


    @Transactional
    public void addMemberCommitAndUpdate(String githubId, String name, String profileImage) {
        List<Commit> commits = commitService.findCommits(githubId);
        Member member = findMemberByGithubId(githubId, AuthStep.NONE);
        member.updateNameAndImage(name, profileImage);
        if (commits.isEmpty()) return;
        commits.forEach(member::addCommit);
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
    public void updateCommits() {
        Member member = authService.getLoginUser();
        getCommitsByScraping(member.getGithubId());
        if (!isWalletAddressExist(member)) return;
        updateTier(member);
    }

    @Transactional
    public MemberResponse getMember() {
        Member member = authService.getLoginUser();
        Integer rank = memberRepository.findRankingById(member.getId());
        Long amount = member.getSumOfTokens();
        updateTier(member);
        OrganizationDetails organizationDetails = member.getOrganizationDetails();

        if (organizationDetails == null) {
            return memberMapper.toResponse(member, member.getSumOfCommits(), rank, amount);
        }

        Organization organization = organizationRepository.findById(organizationDetails.getOrganizationId())
                .orElseThrow(EntityNotFoundException::new);

        return memberMapper.toResponse(member, member.getSumOfCommits(), rank, amount, organization.getName());
    }

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
        blockchainService.setTransaction(
                new ContractRequest(member.getWalletAddress(),
                        ContributeType.COMMIT.toString(),
                        BigInteger.valueOf(member.getSumOfCommits()),
                        member.getGithubId()));
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

    private Member scrapeAndGetSavedMember(String githubId, Role role, AuthStep authStep) {
        getCommitsByScraping(githubId);
        return saveAndGet(githubId, role, authStep);
    }

    private void getCommitsByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }

    private boolean isWalletAddressExist(Member member) {
        return StringUtils.hasText(member.getWalletAddress());
    }
}
