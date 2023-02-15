package com.dragonguard.backend.gitrepomember.mapper;

import com.dragonguard.backend.gitrepo.entity.GitRepo;
import com.dragonguard.backend.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class GitRepoMemberMapper {

    public GitRepoMember toEntity(GitRepoMemberResponse dto, Member member, GitRepo gitRepo) {
        return GitRepoMember.builder()
                .commits(dto.getCommits())
                .additions(dto.getAdditions())
                .deletions(dto.getDeletions())
                .gitRepo(gitRepo)
                .member(member)
                .build();
    }

    public GitRepoMemberResponse toResponse(GitRepoMember gitRepoMember) {
        return GitRepoMemberResponse.builder()
                .githubId(gitRepoMember.getMember().getGithubId())
                .additions(gitRepoMember.getAdditions())
                .deletions(gitRepoMember.getDeletions())
                .commits(gitRepoMember.getCommits())
                .build();
    }
}
