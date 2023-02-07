package com.dragonguard.backend.member.service;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.mapper.MemberMapper;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CommitService commitService;

    public Tier getTier(Long id) {
        return getEntity(id).getTier();
    }

    public Long saveMember(MemberRequest memberRequest) {
        return saveAndGetEntity(memberRequest).getId();
    }

    public Member saveAndGetEntity(MemberRequest memberRequest) {
        getCommitByScraping(memberRequest.getGithubId()); // TODO 이 라인 제외시킬지 논의 필요
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
    }

    @Transactional
    public void updateTier(Member member) {
        Tier tier = Tier.checkTier(member.evaluateCommitsSum());
        member.updateTier(tier);
    }

    @Transactional
    public void updateCommits(Long id) {
        String githubId = memberRepository.findGithubIdById(id);
        getCommitByScraping(githubId);
        updateTier(getEntity(id));
    }

    public MemberResponse getMember(Long id) {
        Member member = getEntity(id);
        Integer rank = memberRepository.findRankingById(id);
        return memberMapper.toResponse(member, member.getCommitsSum(), rank);
    }

    public Member findMemberByGithubId(String githubId) {
        return memberRepository.findMemberByGithubId(githubId)
                .orElseGet(() -> saveAndGetEntity(new MemberRequest(githubId)));
    }

    public List<MemberRankResponse> getMemberRanking(Pageable pageable) {
        return memberRepository.findRanking(pageable);
    }

    private Member getEntity(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void getCommitByScraping(String githubId) {
        commitService.scrapingCommits(githubId);
    }
}
