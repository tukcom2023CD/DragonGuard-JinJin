package com.dragonguard.backend.gitrepomember.service;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.MemberService;
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

    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final MemberService memberService;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void updateOrSaveAll(List<GitRepoMemberResponse> gitRepoResponses, String gitRepo) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepository -> {
            Member member = memberService.saveAndGet(new MemberRequest(gitRepository.getGithubId()));

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
                .forEach(o -> gitRepoMemberRepository.findByGitRepoAndMember(o.getGitRepo(), o.getMember()).update(o));

        list.stream()
                .filter(m -> !gitRepoMemberRepository.existsByGitRepoAndMember(m.getGitRepo(), m.getMember()))
                .forEach(gitRepoMemberRepository::save);
    }

    public void saveAll(List<GitRepoMemberResponse> gitRepoResponses, String gitRepo) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepository -> {
            Member member = memberService.saveAndGet(new MemberRequest(gitRepository.getGithubId()));

            GitRepo gitRepoEntity = gitRepoRepository.findByName(gitRepo)
                    .orElseThrow(EntityNotFoundException::new);

            return gitRepoMemberMapper.toEntity(gitRepository, member, gitRepoEntity);
        }).collect(Collectors.toList());
        gitRepoMemberRepository.saveAll(list);
    }

    public List<GitRepoMember> findAllByGitRepo(GitRepo gitRepo) {
        return gitRepoMemberRepository.findAllByGitRepo(gitRepo);
    }

    public GitRepoMember findByNameAndMemberName(String repoName, String memberName) {
        return gitRepoMemberRepository.findByNameAndMemberName(repoName, memberName);
    }
}
