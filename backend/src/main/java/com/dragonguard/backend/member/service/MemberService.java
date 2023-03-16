package com.dragonguard.backend.member.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.config.security.oauth.OAuth2Request;
import com.dragonguard.backend.config.security.oauth.user.OAuth2UserInfo;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.OAuth2Info;
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

    public Tier getTier(UUID id) {
        return getEntity(id).getTier();
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
        setTransaction(commits.size(), member);
        updateTier(member);
    }

    private void setTransaction(Integer size, Member member) {
        blockchainService.setTransaction(
                new ContractRequest(member.getWalletAddress(),
                        ContributeType.COMMIT.toString(),
                        BigInteger.valueOf(size),
                        member.getGithubId()));
    }

    @Transactional
    public void updateTier(Member member) {
        if (!isWalletAddressExist(member)) return;
        member.updateTier();
    }

    @Transactional
    public void updateCommits(UUID id) {
        Member member = getEntity(id);
        getCommitsByScraping(member.getGithubId());
        if (!isWalletAddressExist(member)) return;
        updateTier(member);
    }

    public MemberResponse getMember(UUID id) {
        Member member = getEntity(id);
        Integer rank = memberRepository.findRankingById(id);
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
    public void updateWalletAddress(WalletRequest walletRequest, UUID memberId) {
        Member member = getEntity(memberId);
        member.updateWalletAddress(walletRequest.getWalletAddress());
        setTransaction(member.getSumOfCommits(), member);
    }

    public Member getEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void getCommitsByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }

    private boolean isWalletAddressExist(Member member) {
        return member.getWalletAddress() != null && !member.getWalletAddress().isEmpty();
    }

    public Member saveIfNone(OAuth2Request oAuth2Request) {
        String email = oAuth2Request.getEmail().orElseThrow(IllegalArgumentException::new);
        getCommitsByScraping(oAuth2Request.getAccountId());
        return memberRepository
                .findByEmail(email)
                .orElseGet(() -> memberRepository.save(setUpMember(oAuth2Request)));
    }

    private Member setUpMember(OAuth2Request req) {
        Member.MemberBuilder memberBuilder = Member.builder();
        req.getName().ifPresent(memberBuilder::name);
        req.getEmail().ifPresent(memberBuilder::email);
        memberBuilder.githubId(req.getAccountId());
        return memberBuilder.build();
    }
}
