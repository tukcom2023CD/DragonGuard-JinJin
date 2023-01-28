package com.dragonguard.backend.member.service;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.service.CommitService;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.mapper.MemberMapper;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        getCommitByScrapping(memberRequest.getGithubId());
        return memberRepository
                .save(memberMapper.toEntity(memberRequest)).getId();
    }

    @Transactional
    public void addMemberCommit(String githubId) {
        List<Commit> commits = commitService.findCommits(githubId);
        Member member = memberRepository.findMemberByGithubId(githubId)
                .orElseThrow(EntityNotFoundException::new);
        commits.stream().forEach(member::addCommit);
        updateTier(member.getId());
        commitService.saveAllCommits(commits);
    }

    @Transactional
    public void updateCommits(Long id) {
        String githubId = memberRepository.findGithubIdById(id);
        getCommitByScrapping(githubId);
        updateTier(id);
    }

    private Member getEntity(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void getCommitByScrapping(String githubId) {
        commitService.scrappingCommits(githubId);
    }

    @Transactional
    public void updateTier(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Tier tier = Tier.checkTier(member.getCommitsSum());
        member.updateTier(tier);
    }
}
