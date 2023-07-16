package com.dragonguard.backend.domain.gitrepomember.mapper;

import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoContribution;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 레포지토리 기여자 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface GitRepoMemberMapper extends EntityMapper {
    @Mapping(target = "gitRepoContribution", expression = "java(new GitRepoContribution(response.getCommits(), response.getAdditions(), response.getDeletions()))")
    GitRepoMember toEntity(final GitRepoMemberResponse response, final Member member, final GitRepo gitRepo);
    GitRepoMember toEntity(final Member member, final GitRepo gitRepo);
    GitRepoMember toEntity(final Member member, final GitRepo gitRepo, final GitRepoContribution gitRepoContribution);
    @Mapping(target = "githubId", source = "gitRepoMember.member.githubId")
    @Mapping(target = "commits", source = "gitRepoMember.gitRepoContribution.commits")
    @Mapping(target = "additions", source = "gitRepoMember.gitRepoContribution.additions")
    @Mapping(target = "deletions", source = "gitRepoMember.gitRepoContribution.deletions")
    @Mapping(target = "profileUrl", source = "gitRepoMember.member.profileImage")
    @Mapping(target = "isServiceMember", expression = "java(gitRepoMember.getMember().isServiceMember())")
    GitRepoMemberResponse toResponse(final GitRepoMember gitRepoMember);

    default List<GitRepoMemberResponse> toResponseList(Set<GitRepoMember> gitRepoMembers) {
        return gitRepoMembers.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
