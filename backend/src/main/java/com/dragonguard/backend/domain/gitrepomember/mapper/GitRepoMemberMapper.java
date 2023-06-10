package com.dragonguard.backend.domain.gitrepomember.mapper;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author 김승진
 * @description 깃허브 레포지토리 기여자 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface GitRepoMemberMapper {
    @Mapping(target = "gitRepoContribution", expression = "java(new GitRepoContribution(response.getCommits(), response.getAdditions(), response.getDeletions()))")
    GitRepoMember toEntity(final GitRepoMemberResponse response, final Member member, final GitRepo gitRepo);
    GitRepoMember toEntity(final Member member, final GitRepo gitRepo);
    @Mapping(target = "githubId", source = "gitRepoMember.member.githubId")
    @Mapping(target = "commits", source = "gitRepoMember.gitRepoContribution.commits")
    @Mapping(target = "additions", source = "gitRepoMember.gitRepoContribution.additions")
    @Mapping(target = "deletions", source = "gitRepoMember.gitRepoContribution.deletions")
    GitRepoMemberResponse toResponse(final GitRepoMember gitRepoMember);
}
