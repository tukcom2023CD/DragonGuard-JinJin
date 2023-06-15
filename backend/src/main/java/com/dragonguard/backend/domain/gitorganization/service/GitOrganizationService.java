package com.dragonguard.backend.domain.gitorganization.service;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.mapper.GitOrganizationMapper;
import com.dragonguard.backend.domain.gitorganization.repository.GitOrganizationRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

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

    public void findAndSaveGitOrganizations(final Set<String> gitOrganizationNames, final Member member) {
        Set<GitOrganization> gitOrganizations = getNotSavedGitOrganizations(gitOrganizationNames, member);

        saveAllGitOrganizations(gitOrganizations);
    }

    public Set<GitOrganization> getNotSavedGitOrganizations(final Set<String> gitOrganizationNames, final Member member) {
        return gitOrganizationNames.stream()
                .filter(name -> !gitOrganizationRepository.existsByName(name))
                .map(name -> gitOrganizationMapper.toEntity(name, member))
                .collect(Collectors.toSet());
    }

    public void saveAllGitOrganizations(final Set<GitOrganization> gitOrganizations) {
        try{
            gitOrganizationRepository.saveAll(gitOrganizations);
        } catch(DataIntegrityViolationException e) {}
    }

    public List<GitOrganization> findGitOrganizationByMember(final Member member) {
        return gitOrganizationRepository.findAllByMember(member);
    }

    @Override
    public GitOrganization loadEntity(final Long id) {
        return gitOrganizationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
