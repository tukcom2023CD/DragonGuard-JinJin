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
        gitRepoMembers.forEach(gitRepoMember -> {
            gitRepoMemberQueryRepository.findByGitRepoAndMember(
                    gitRepoMember.getGitRepo(),
                    gitRepoMember.getMember())
                    .orElseGet(() -> gitRepoRepository.save(gitRepoMember))
                    .update(gitRepoMember);
            gitRepoMemberRepository.save(gitRepoMember);
        });
    }

    public List<GitRepoMember> validateAndGetGitRepoMembers(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        return gitRepoResponses.stream()
                .map(gitRepoResponse -> getNotDuplicatedGitRepoMembers(gitRepoName, gitRepoResponse))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public GitRepoMember getNotDuplicatedGitRepoMembers(final String gitRepoName, final GitRepoMemberResponse gitRepoResponse) {
        GitRepo gitRepo = getGitRepoByName(gitRepoName);
        GitRepoMember gitRepoMember = gitRepoMemberMapper.toEntity(gitRepoResponse, getMemberByGitRepoResponse(gitRepoResponse), gitRepo);
        return getGitRepoMemberIfNotDuplicated(gitRepo, gitRepoMember);
    }

    public GitRepoMember getGitRepoMemberIfNotDuplicated(final GitRepo gitRepo, final GitRepoMember gitRepoMember) {
        List<GitRepoMember> duplicated = getDuplicatedGitRepoMembers(gitRepo, gitRepoMember);
        if (duplicated.isEmpty()) return gitRepoMember;
        return null;
    }

    public List<GitRepoMember> getDuplicatedGitRepoMembers(final GitRepo gitRepo, final GitRepoMember gitRepoMember) {
        return gitRepoMemberQueryRepository.findAllByGitRepo(gitRepo).stream()
                .filter(gitRepoMember::customEquals)
                .collect(Collectors.toList());
    }

    public void saveAll(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        try {
            gitRepoMemberRepository.saveAll(getGitRepoMembers(gitRepoResponses, gitRepoName));
        } catch (DataIntegrityViolationException e) {}
    }

    public List<GitRepoMember> getGitRepoMembers(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        return gitRepoResponses.stream()
                .map(gitRepository ->
                        getGitRepoMember(gitRepository, getMemberByGitRepoResponse(gitRepository),
                                getGitRepoByName(gitRepoName)))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public GitRepoMember getGitRepoMember(final GitRepoMemberResponse gitRepository, final Member member, final GitRepo gitRepo) {
        if (gitRepoMemberQueryRepository.existsByGitRepoAndMember(gitRepo, member)) return null;
        return gitRepoMemberMapper.toEntity(gitRepository, member, gitRepo);
    }

    public GitRepo getGitRepoByName(final String gitRepoName) {
        return gitRepoRepository.findByName(gitRepoName)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member getMemberByGitRepoResponse(final GitRepoMemberResponse gitRepository) {
        return memberService.findMemberOrSave(new MemberRequest(gitRepository.getGithubId()), AuthStep.NONE);
    }

    public List<GitRepoMember> findAllByGitRepo(final GitRepo gitRepo) {
        return gitRepoMemberQueryRepository.findAllByGitRepo(gitRepo);
    }

    public GitRepoMember findByNameAndMemberName(final String repoName, final String memberName) {
        return gitRepoMemberQueryRepository.findByNameAndMemberName(repoName, memberName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public GitRepoMember loadEntity(final Long id) {
        return gitRepoMemberQueryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
