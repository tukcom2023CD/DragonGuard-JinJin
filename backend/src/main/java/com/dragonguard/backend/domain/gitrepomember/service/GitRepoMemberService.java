package com.dragonguard.backend.domain.gitrepomember.service;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.JpaGitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.repository.JpaGitRepoMemberRepository;
import com.dragonguard.backend.domain.member.dto.request.MemberRequest;
import com.dragonguard.backend.domain.member.entity.AuthStep;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description GitRepoMember 관련 서비스로직을 담당하는 클래스
 */

@Service
@RequiredArgsConstructor
public class GitRepoMemberService {

    private final JpaGitRepoMemberRepository gitRepoMemberRepository;
    private final MemberService memberService;
    private final JpaGitRepoRepository gitRepoRepository;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void updateOrSaveAll(List<GitRepoMemberResponse> gitRepoResponses, String gitRepo) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepository -> {
            Member member = memberService.saveAndGet(new MemberRequest(gitRepository.getGithubId()), AuthStep.NONE);

            GitRepo gitRepoEntity = gitRepoRepository.findByName(gitRepo)
                    .orElseThrow(EntityNotFoundException::new);

            GitRepoMember gitRepoMember = gitRepoMemberMapper.toEntity(gitRepository, member, gitRepoEntity);
            List<GitRepoMember> duplicated =
                    gitRepoMemberRepository.findAllByGitRepo(gitRepoEntity).stream()
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

    public void saveAll(List<GitRepoMemberResponse> gitRepoResponses, String gitRepo) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepository -> {
            Member member = memberService.saveAndGet(new MemberRequest(gitRepository.getGithubId()), AuthStep.NONE);

            GitRepo gitRepoEntity = gitRepoRepository.findByName(gitRepo)
                    .orElseThrow(EntityNotFoundException::new);

            if (gitRepoMemberRepository.existsByGitRepoAndMember(gitRepoEntity, member)) {
                return null;
            }

            return gitRepoMemberMapper.toEntity(gitRepository, member, gitRepoEntity);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        gitRepoMemberRepository.saveAll(list);
    }

    public List<GitRepoMember> findAllByGitRepo(GitRepo gitRepo) {
        return gitRepoMemberRepository.findAllByGitRepo(gitRepo);
    }

    public GitRepoMember findByNameAndMemberName(String repoName, String memberName) {
        return gitRepoMemberRepository.findByNameAndMemberName(repoName, memberName)
                .orElseThrow(EntityNotFoundException::new);
    }
}
