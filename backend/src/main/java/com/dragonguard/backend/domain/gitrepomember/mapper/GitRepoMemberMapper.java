package com.dragonguard.backend.domain.gitrepomember.mapper;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.Contribution;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description GitRepoMember Entity 와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class GitRepoMemberMapper {

    public GitRepoMember toEntity(GitRepoMemberResponse dto, Member member, GitRepo gitRepo) {
        return GitRepoMember.builder()
                .contribution(new Contribution(dto.getCommits(), dto.getAdditions(), dto.getDeletions()))
                .gitRepo(gitRepo)
                .member(member)
                .build();
    }

    public GitRepoMemberResponse toResponse(GitRepoMember gitRepoMember) {
        Contribution contribution = gitRepoMember.getContribution();
        return GitRepoMemberResponse.builder()
                .githubId(gitRepoMember.getMember().getGithubId())
                .additions(contribution.getAdditions())
                .deletions(contribution.getDeletions())
                .commits(contribution.getCommits())
                .build();
    }
}
