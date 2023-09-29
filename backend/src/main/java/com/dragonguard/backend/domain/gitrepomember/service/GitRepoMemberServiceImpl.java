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
import com.dragonguard.backend.global.template.service.TransactionService;
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
public class GitRepoMemberServiceImpl implements GitRepoMemberService {
    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final MemberService memberService;

    @Override
    public void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoResponses, final GitRepo gitRepo) {
        final List<GitRepoMember> gitRepoMembers = validateAndGetGitRepoMembers(gitRepoResponses, gitRepo);
        saveOrUpdateGitRepoMembers(gitRepoMembers);
    }

    @Override
    public void saveAllIfNotExists(final Member member, final Set<GitRepo> gitRepos) {
        final List<GitRepoMember> gitRepoMembers = gitRepos.stream()
                .map(gitRepo -> findGitRepoMember(member, gitRepo))
                .collect(Collectors.toList());

        gitRepoMemberRepository.saveAll(gitRepoMembers);
    }

    private GitRepoMember findGitRepoMember(final Member member, final GitRepo gitRepo) {
        return gitRepoMemberRepository.findByGitRepoAndMember(gitRepo, member)
                .orElseGet(() -> gitRepoMemberRepository.save(gitRepoMemberMapper.toEntity(member, gitRepo)));
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
        final String githubId = response.getGithubId();
        final GitRepoMember gitRepoMember = gitRepoMemberRepository.findByGitRepoAndMemberGithubId(gitRepo, githubId)
                .orElseGet(() -> gitRepoMemberRepository.save(
                            gitRepoMemberMapper.toEntity(response.getMember(), gitRepo)));
        gitRepoMember.updateProfileImageAndContribution(response.getProfileUrl(), response.getCommits(), response.getAdditions(), response.getDeletions());
        return gitRepoMember;
    }

    @Override
    public void saveAllIfNotExists(final List<GitRepoMemberResponse> gitRepoResponses, final GitRepo gitRepo) {
        gitRepoMemberRepository.saveAll(findGitRepoMembers(gitRepoResponses, gitRepo));
    }

    private List<GitRepoMember> findGitRepoMembers(final List<GitRepoMemberResponse> gitRepoResponses, final GitRepo gitRepo) {
        return gitRepoResponses.stream()
                .distinct()
                .map(gitRepoResponse ->
                        findGitRepoMember(gitRepoResponse, findMemberByGitRepoResponse(gitRepoResponse), gitRepo))
                .collect(Collectors.toList());
    }

    private GitRepoMember findGitRepoMember(final GitRepoMemberResponse response, final Member member, final GitRepo gitRepo) {
        final GitRepoMember gitRepoMember = gitRepoMemberRepository.findByGitRepoAndMember(gitRepo, member)
                .orElseGet(() -> gitRepoMemberMapper.toEntity(member, gitRepo));

        gitRepoMember.updateProfileImageAndContribution(
                response.getProfileUrl(),
                response.getCommits(),
                response.getAdditions(),
                response.getDeletions());

        return gitRepoMember;
    }

    private Member findMemberByGitRepoResponse(final GitRepoMemberResponse gitRepoMemberResponse) {
        return memberService.saveIfNone(gitRepoMemberResponse.getGithubId(), AuthStep.NONE, gitRepoMemberResponse.getProfileUrl());
    }

    @Override
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
    public Boolean isServiceMember(final String githubId) {
        return memberService.isServiceMember(githubId);
    }
}
