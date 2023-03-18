package com.dragonguard.backend.member.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.config.security.oauth.OAuth2Request;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.mapper.MemberMapper;
import com.dragonguard.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Tier getTier() {
        return authService.getLoginUser().getTier();
    }

    public IdResponse<UUID> saveMember(MemberRequest memberRequest) {
        return new IdResponse<>(scrapeAndGetId(memberRequest));
    }

    public UUID scrapeAndGetId(MemberRequest memberRequest) {
        return scrapeAndGetSavedMember(memberRequest).getId();
    }

    private Member scrapeAndGetSavedMember(MemberRequest memberRequest) {
        getCommitsByScraping(memberRequest.getGithubId());
        return saveAndGet(memberRequest);
    }

    public Member saveAndGet(MemberRequest memberRequest) {
        if (memberRepository.existsByGithubId(memberRequest.getGithubId())) {
            return memberRepository.findMemberByGithubId(memberRequest.getGithubId())
                    .orElseThrow(EntityNotFoundException::new);
        }
        return memberRepository
                .save(memberMapper.toEntity(memberRequest));
    }


    @Transactional
    public void addMemberCommitAndUpdate(String githubId, String name, String profileImage) {
        List<Commit> commits = commitService.findCommits(githubId);
        Member member = findMemberByGithubId(githubId);
        member.updateNameAndImage(name, profileImage);
        if (commits.isEmpty()) return;
        commits.forEach(member::addCommit);
        if (!isWalletAddressExist(member)) {
            updateTier(member);
            return;
        }
        setTransaction(commits.size(), member.getWalletAddress(), member.getGithubId());
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

    public MemberResponse getMember() {
        Member member = authService.getLoginUser();
        Integer rank = memberRepository.findRankingById(member.getId());
        Long amount = member.getSumOfTokens();
        updateTier(member);

        return memberMapper.toResponse(member, member.getSumOfCommits(), rank, amount);
    }

    public Member findMemberByGithubId(String githubId) {
        return memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> scrapeAndGetSavedMember(new MemberRequest(githubId)));
    }

    public List<MemberRankResponse> getMemberRanking(Pageable pageable) {
        return memberRepository.findRanking(pageable);
    }

    @Transactional
    public void updateWalletAddress(WalletRequest walletRequest) {
        Member member = authService.getLoginUser();
        member.updateWalletAddress(walletRequest.getWalletAddress());
        if (member.getCommits().isEmpty()) {
            return;
        }
        setTransaction(member.getCommits().stream().mapToInt(i -> i.getCommitNum()).sum(), member.getWalletAddress(), member.getGithubId());
    }

    public Member getEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member saveIfNone(OAuth2Request oAuth2Request) {
        String githubId = oAuth2Request.getAccountId();
        Member member = memberRepository
                .findMemberByGithubId(githubId)
                .orElseGet(() -> memberRepository.save(setUpMember(oAuth2Request)));
        getCommitsByScraping(githubId);
        return member;
    }

    private void setTransaction(Integer size, String walletAddress, String githubId) {
        blockchainService.setTransaction(
                new ContractRequest(walletAddress,
                        ContributeType.COMMIT.toString(),
                        BigInteger.valueOf(size),
                        githubId));
    }

    private void getCommitsByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }

    private boolean isWalletAddressExist(Member member) {
        return member.getWalletAddress() != null && !member.getWalletAddress().isEmpty();
    }

    private Member setUpMember(OAuth2Request req) {
        return Member.builder().githubId(req.getAccountId()).build();
    }
}
