package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberQueryRepository;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description GitRepoMember 관련 서비스로직을 담당하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class GitRepoMemberService implements EntityLoader<GitRepoMember, Long> {

    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final GitRepoMemberQueryRepository gitRepoMemberQueryRepository;
    private final MemberService memberService;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        List<GitRepoMember> gitRepoMembers = validateAndGetGitRepoMembers(gitRepoResponses, gitRepoName);
        saveOrUpdateGitRepoMembers(gitRepoMembers);
    }

    public void saveOrUpdateGitRepoMembers(final List<GitRepoMember> gitRepoMembers) {
        try {
            gitRepoMembers.forEach(gitRepoMember -> gitRepoMemberQueryRepository.findByGitRepoAndMember(
                            gitRepoMember.getGitRepo(),
                            gitRepoMember.getMember())
                    .orElseGet(() -> gitRepoMemberRepository.save(gitRepoMember))
                    .update(gitRepoMember));
        } catch (DataIntegrityViolationException e) {}
    }

    public List<GitRepoMember> validateAndGetGitRepoMembers(final List<GitRepoMemberResponse> gitRepoMemberResponses, final String gitRepoName) {
        return gitRepoMemberResponses.stream()
                .map(gitRepoResponse -> findByNameAndMemberGithubId(gitRepoName, gitRepoResponse.getGithubId()))
                .collect(Collectors.toList());
    }

    public void saveAll(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        try {
            gitRepoMemberRepository.saveAll(getGitRepoMembers(gitRepoResponses, gitRepoName));
        } catch (DataIntegrityViolationException e) {}
    }

    public List<GitRepoMember> getGitRepoMembers(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        return gitRepoResponses.stream()
                .map(gitRepo ->
                        getGitRepoMember(gitRepo, getMemberByGitRepoResponse(gitRepo),
                                getGitRepoByName(gitRepoName)))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public GitRepoMember getGitRepoMember(final GitRepoMemberResponse gitRepoMemberResponse, final Member member, final GitRepo gitRepo) {
        GitRepoMember gitRepoMember = gitRepoMemberQueryRepository.findByGitRepoAndMember(gitRepo, member)
                .orElse(gitRepoMemberMapper.toEntity(member, gitRepo));

        gitRepoMember.updateGitRepoContribution(
                gitRepoMemberResponse.getCommits(),
                gitRepoMemberResponse.getAdditions(),
                gitRepoMemberResponse.getDeletions());

        return gitRepoMember;
    }

    public GitRepo getGitRepoByName(final String gitRepoName) {
        return gitRepoRepository.findByName(gitRepoName)
                .orElse(gitRepoRepository.save(new GitRepo(gitRepoName)));
    }

    public Member getMemberByGitRepoResponse(final GitRepoMemberResponse gitRepository) {
        return memberService.findMemberOrSave(new MemberRequest(gitRepository.getGithubId()), AuthStep.NONE);
    }

    public List<GitRepoMember> findAllByGitRepo(final GitRepo gitRepo) {
        return gitRepoMemberQueryRepository.findAllByGitRepo(gitRepo);
    }

    public GitRepoMember findByNameAndMemberGithubId(final String gitRepoName, final String githubId) {
        return gitRepoMemberQueryRepository.findByNameAndMemberGithubId(gitRepoName, githubId)
                .orElse(gitRepoMemberMapper.toEntity(memberService.findByGithubIdOrSaveWithAuthStep(githubId, AuthStep.NONE), getGitRepoByName(gitRepoName)));
    }

    @Override
    public GitRepoMember loadEntity(final Long id) {
        return gitRepoMemberQueryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
