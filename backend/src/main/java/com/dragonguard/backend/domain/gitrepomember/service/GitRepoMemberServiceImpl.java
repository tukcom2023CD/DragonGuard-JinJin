package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.entity.Role;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description GitRepoMember 관련 서비스로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class GitRepoMemberServiceImpl implements EntityLoader<GitRepoMember, Long>, GitRepoMemberService {
    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final MemberService memberService;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoResponses, final GitRepo gitRepo) {
        List<GitRepoMember> gitRepoMembers = validateAndGetGitRepoMembers(gitRepoResponses, gitRepo);
        saveOrUpdateGitRepoMembers(gitRepoMembers);
    }

    private void saveOrUpdateGitRepoMembers(final List<GitRepoMember> gitRepoMembers) {
        gitRepoMembers.forEach(gitRepoMember -> gitRepoMemberRepository.findByGitRepoAndMember(
                        gitRepoMember.getGitRepo(),
                        gitRepoMember.getMember())
                .orElseGet(() -> {
                    GitRepoMember savedGitRepoMember = gitRepoMemberRepository.save(gitRepoMember);
                    return gitRepoMemberRepository.findById(savedGitRepoMember.getId())
                            .orElseThrow(EntityNotFoundException::new);
                })
                .update(gitRepoMember));
    }

    private List<GitRepoMember> validateAndGetGitRepoMembers(final List<GitRepoMemberResponse> gitRepoMemberResponses, final GitRepo gitRepo) {
        return gitRepoMemberResponses.stream()
                .map(gitRepoResponse -> saveAndGetGitRepoMember(gitRepoResponse, gitRepo))
                .collect(Collectors.toList());
    }

    private GitRepoMember saveAndGetGitRepoMember(final GitRepoMemberResponse response, final GitRepo gitRepo) {
        String githubId = response.getGithubId();
        GitRepoMember gitRepoMember = gitRepoMemberRepository.findByGitRepoAndMemberGithubId(gitRepo, githubId)
                .orElseGet(() -> gitRepoMemberRepository.save(
                            gitRepoMemberMapper.toEntity(memberService.findMemberOrSave(githubId, AuthStep.NONE, response.getProfileUrl()), gitRepo)));
        gitRepoMember.updateProfileImageAndContribution(response.getProfileUrl(), response.getCommits(), response.getAdditions(), response.getDeletions());
        return gitRepoMember;
    }


    public void saveAll(final List<GitRepoMemberResponse> gitRepoResponses, final GitRepo gitRepo) {
        gitRepoMemberRepository.saveAll(getGitRepoMembers(gitRepoResponses, gitRepo));
    }

    private List<GitRepoMember> getGitRepoMembers(final List<GitRepoMemberResponse> gitRepoResponses, final GitRepo gitRepo) {
        return gitRepoResponses.stream()
                .distinct()
                .map(gitRepoResponse ->
                        getGitRepoMember(gitRepoResponse, getMemberByGitRepoResponse(gitRepoResponse), gitRepo))
                .collect(Collectors.toList());
    }

    private GitRepoMember getGitRepoMember(final GitRepoMemberResponse gitRepoMemberResponse, final Member member, final GitRepo gitRepo) {
        GitRepoMember gitRepoMember = gitRepoMemberRepository.findByGitRepoAndMember(gitRepo, member)
                .orElseGet(() -> gitRepoMemberMapper.toEntity(member, gitRepo));

        gitRepoMember.updateGitRepoContribution(
                gitRepoMemberResponse.getCommits(),
                gitRepoMemberResponse.getAdditions(),
                gitRepoMemberResponse.getDeletions());

        return gitRepoMember;
    }

    private Member getMemberByGitRepoResponse(final GitRepoMemberResponse gitRepoMemberResponse) {
        return memberService.findMemberOrSave(gitRepoMemberResponse.getGithubId(), AuthStep.NONE, gitRepoMemberResponse.getProfileUrl());
    }

    public GitRepoMember findByGitRepoAndMemberGithubId(final GitRepo gitRepo, final String githubId) {
        return gitRepoMemberRepository.findByGitRepoAndMemberGithubId(gitRepo, githubId)
                .orElseGet(() -> gitRepoMemberRepository.save(
                            gitRepoMemberMapper.toEntity(
                                    memberService.findMemberOrSaveWithRole(githubId, Role.ROLE_USER, AuthStep.NONE), gitRepo)));
    }

    @Override
    public GitRepoMember loadEntity(final Long id) {
        return gitRepoMemberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void saveAllGitRepoMembers(final Set<GitRepoMember> gitRepoMembers) {
        gitRepoMemberRepository.saveAll(gitRepoMembers);
    }

    public Boolean isServiceMember(final String githubId) {
        return memberService.isServiceMember(githubId);
    }
}
