package com.dragonguard.backend.domain.gitorganization.service;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.mapper.GitOrganizationMapper;
import com.dragonguard.backend.domain.gitorganization.repository.GitOrganizationRepository;
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

    public void saveGitOrganizations(final Set<String> gitOrganizationNames, final Member member) {
        Set<GitOrganization> gitOrganizations = gitOrganizationNames.stream()
                .map(name -> gitOrganizationRepository.findByName(name).orElseGet(() -> gitOrganizationMapper.toEntity(name, member)))
                .collect(Collectors.toSet());

        gitOrganizationRepository.saveAll(gitOrganizations);
    }

    public List<GitOrganization> findGitOrganizationByGithubId(final String githubId) {
        return gitOrganizationRepository.findAllByGithubId(githubId);
    }

    @Override
    public GitOrganization loadEntity(final Long id) {
        return gitOrganizationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
