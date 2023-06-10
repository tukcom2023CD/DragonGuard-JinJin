package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

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
    private final MemberService memberService;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void updateOrSaveAll(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepoName) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepoResponse -> {
            Member member = memberService.saveAndGet(new MemberRequest(gitRepoResponse.getGithubId()), AuthStep.NONE);

            GitRepo gitRepo = gitRepoRepository.findByName(gitRepoName)
                    .orElseThrow(EntityNotFoundException::new);

            GitRepoMember gitRepoMember = gitRepoMemberMapper.toEntity(gitRepoResponse, member, gitRepo);

            List<GitRepoMember> duplicated =
                    gitRepoMemberRepository.findAllByGitRepo(gitRepo).stream()
                            .filter(gitRepoMember::equals)
                            .collect(Collectors.toList());

            if (duplicated.isEmpty()) return gitRepoMember;
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        list.stream()
                .filter(m -> gitRepoMemberRepository.existsByGitRepoAndMember(m.getGitRepo(), m.getMember()))
                .forEach(o -> gitRepoMemberRepository.findByGitRepoAndMember(o.getGitRepo(), o.getMember())
                        .orElseThrow(EntityNotFoundException::new).update(o));

        list.stream()
                .filter(m -> !gitRepoMemberRepository.existsByGitRepoAndMember(m.getGitRepo(), m.getMember()))
                .forEach(gitRepoMemberRepository::save);
    }

    public void saveAll(final List<GitRepoMemberResponse> gitRepoResponses, final String gitRepo) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepository -> {
            Member member = memberService.saveAndGet(new MemberRequest(gitRepository.getGithubId()), AuthStep.NONE);

            GitRepo gitRepoEntity = gitRepoRepository.findByName(gitRepo)
                    .orElseThrow(EntityNotFoundException::new);

            if (gitRepoMemberRepository.existsByGitRepoAndMember(gitRepoEntity, member)) return null;

            return gitRepoMemberMapper.toEntity(gitRepository, member, gitRepoEntity);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        gitRepoMemberRepository.saveAll(list);
    }

    public List<GitRepoMember> findAllByGitRepo(final GitRepo gitRepo) {
        return gitRepoMemberRepository.findAllByGitRepo(gitRepo);
    }

    public GitRepoMember findByNameAndMemberName(final String repoName, final String memberName) {
        return gitRepoMemberRepository.findByNameAndMemberName(repoName, memberName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public GitRepoMember loadEntity(final Long id) {
        return gitRepoMemberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
