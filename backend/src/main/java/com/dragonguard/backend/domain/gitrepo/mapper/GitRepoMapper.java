package com.dragonguard.backend.domain.gitrepo.mapper;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 레포지토리 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface GitRepoMapper {
    GitRepo toEntity(final String name);

    @Mapping(target = "commits", source = "gitRepoMember.gitRepoContribution.commits")
    @Mapping(target = "additions", source = "gitRepoMember.gitRepoContribution.additions")
    @Mapping(target = "deletions", source = "gitRepoMember.gitRepoContribution.deletions")
    @Mapping(target = "githubId", source = "gitRepoMember.member.githubId")
    @Mapping(target = "isServiceMember", expression = "java(gitRepoMember.getMember().isServiceMember())")
    GitRepoMemberResponse toGitRepoMemberResponse(GitRepoMember gitRepoMember);

    default List<GitRepoMemberResponse> toGitRepoMemberResponseList(Set<GitRepoMember> gitRepoMembers) {
        return gitRepoMembers.stream().map(this::toGitRepoMemberResponse).collect(Collectors.toList());
    }
}
