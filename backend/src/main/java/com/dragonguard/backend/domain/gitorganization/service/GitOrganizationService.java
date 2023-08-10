package com.dragonguard.backend.domain.gitorganization.service;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.mapper.GitOrganizationMapper;
import com.dragonguard.backend.domain.gitorganization.repository.GitOrganizationRepository;
import com.dragonguard.backend.domain.member.dto.client.MemberOrganizationResponse;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Organization 관련 서비스 로직을 담당하는 Service
 */

@TransactionService
@RequiredArgsConstructor
public class GitOrganizationService implements EntityLoader<GitOrganization, Long> {
    private final GitOrganizationRepository gitOrganizationRepository;
    private final GitOrganizationMapper gitOrganizationMapper;

    public void findAndSaveGitOrganizations(final Set<MemberOrganizationResponse> gitOrganizationNames, final Member member) {
        Set<GitOrganization> gitOrganizations = findNotSavedGitOrganizations(gitOrganizationNames, member);

        saveAllGitOrganizations(gitOrganizations);
    }

    private Set<GitOrganization> findNotSavedGitOrganizations(final Set<MemberOrganizationResponse> gitOrganizationNames, final Member member) {
        return gitOrganizationNames.stream()
                .map(org -> {
                    String gitOrganizationName = org.getLogin();
                    if (!gitOrganizationRepository.existsByName(gitOrganizationName)) {
                        return gitOrganizationMapper.toEntity(gitOrganizationName, org.getAvatarUrl(), member);
                    }
                    GitOrganization gitOrganization = gitOrganizationRepository.findByName(gitOrganizationName)
                            .orElseThrow(EntityNotFoundException::new);
                    gitOrganization.addGitOrganizationMember(member);

                    return gitOrganization;
                }).collect(Collectors.toSet());
    }

    private void saveAllGitOrganizations(final Set<GitOrganization> gitOrganizations) {
        gitOrganizationRepository.saveAll(gitOrganizations);
    }

    public List<GitOrganization> findGitOrganizationByMember(final Member member) {
        return gitOrganizationRepository.findAllByMember(member);
    }

    @Override
    public GitOrganization loadEntity(final Long id) {
        return gitOrganizationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public GitOrganization getByName(final String name) {
        return gitOrganizationRepository.findByName(name)
                .orElseThrow(EntityNotFoundException::new);
    }
}
