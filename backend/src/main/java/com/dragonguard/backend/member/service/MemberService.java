package com.dragonguard.backend.member.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
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

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CommitService commitService;
    private final BlockchainService blockchainService;

    public Tier getTier(Long id) {
        return getEntity(id).getTier();
    }

    public Long saveMember(MemberRequest memberRequest) {
        return scrapeAndSave(memberRequest);
    }

    public Long scrapeAndSave(MemberRequest memberRequest) {
        return scrape(memberRequest).getId();
    }

    private Member scrape(MemberRequest memberRequest) {
        getCommitByScraping(memberRequest.getGithubId());
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
        if (commits.isEmpty()) {
            return;
        }
        commits.forEach(member::addCommit);
        updateTier(member);
        commitService.saveAllCommits(commits);
        if (member.getWalletAddress() == null || member.getWalletAddress().isEmpty()) {
            return;
        }
        setTransaction(commits.size(), member);
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
        Tier tier = Tier.checkTier(member.evaluateCommitsSum());
        member.updateTier(tier);
    }

    @Transactional
    public void updateCommits(Long id) {
        Member member = getEntity(id);
        getCommitByScraping(member.getGithubId());
        updateTier(member);
    }

    public MemberResponse getMember(Long id) {
        Member member = getEntity(id);
        Integer rank = memberRepository.findRankingById(id);
        return memberMapper.toResponse(member, member.getCommitsSum(), rank);
    }

    public Member findMemberByGithubId(String githubId) {
        return memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> scrape(new MemberRequest(githubId)));
    }

    public List<MemberRankResponse> getMemberRanking(Pageable pageable) {
        return memberRepository.findRanking(pageable);
    }

    @Transactional
    public void updateWalletAddress(WalletRequest walletRequest) {
        Member member = getEntity(walletRequest.getId());
        member.updateWalletAddress(walletRequest.getWalletAddress());
        setTransaction(member.getCommitsSum(), member);
    }

    private Member getEntity(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void getCommitByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }
}
