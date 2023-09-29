package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.gitorganization.service.GitOrganizationService;
import com.dragonguard.backend.domain.gitrepo.service.GitRepoMemberFacade;
import com.dragonguard.backend.domain.member.dto.client.MemberOrganizationResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.template.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@TransactionService
@RequiredArgsConstructor
public class GitOrganizationGitRepoService {
    private final GitOrganizationService gitOrganizationService;
    private final GitRepoMemberFacade gitRepoMemberFacade;

    public void saveAll(final Set<String> gitRepoNames, final Set<MemberOrganizationResponse> organizationResponses, final Member member) {
        gitRepoMemberFacade.saveAllGitRepos(gitRepoNames);
        gitRepoMemberFacade.saveAllGitRepoMembers(member, gitRepoNames);
        gitOrganizationService.findAndSaveGitOrganizations(organizationResponses, member);
    }
}
