package com.dragonguard.backend.gitrepomember.service;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepo.service.GitRepoService;
import com.dragonguard.backend.gitrepomember.dto.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitRepoMemberService {

    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final MemberService memberService;
    private final GitRepoService gitRepoService;
    private final GitRepoMemberMapper gitRepoMemberMapper;

    public void saveAllDto(List<GitRepoMemberResponse> gitRepoResponses, String gitRepo) {
        List<GitRepoMember> list = gitRepoResponses.stream().map(gitRepository -> {
            Member member = memberService.saveAndGetEntity(new MemberRequest(gitRepository.getGithubId()));
            GitRepo gitRepoEntity = gitRepoService.findGitRepoByName(gitRepo);
            GitRepoMember gitRepoMember = gitRepoMemberMapper.toEntity(gitRepository, member, gitRepoEntity);
            List<GitRepoMember> duplicated =
                    gitRepoMemberRepository.findAllByGitRepo(gitRepoEntity).stream()
                            .filter(gitRepoMember::equals)
                            .collect(Collectors.toList());
            if (duplicated.isEmpty()) return gitRepoMember;
            return null;
        }).collect(Collectors.toList());

        list.stream().forEach(m -> {
            if (m != null) {
                gitRepoMemberRepository.saveAll(list);
            }
        });
    }
}
