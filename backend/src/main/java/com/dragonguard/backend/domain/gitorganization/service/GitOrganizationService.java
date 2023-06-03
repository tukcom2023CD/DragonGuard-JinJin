package com.dragonguard.backend.domain.gitorganization.service;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.mapper.GitOrganizationMapper;
import com.dragonguard.backend.domain.gitorganization.repository.GitOrganizationRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Organization 관련 서비스 로직을 담당하는 Service
 */

@Service
@RequiredArgsConstructor
public class GitOrganizationService {
    private final GitOrganizationRepository gitOrganizationRepository;
    private final GitOrganizationMapper gitOrganizationMapper;

    @Transactional
    public void saveGitOrganizations(Set<String> gitOrganizationNames, Member member) {
        Set<GitOrganization> gitOrganizations = gitOrganizationNames.stream()
                .map(name -> gitOrganizationRepository.findByName(name).orElseGet(() -> gitOrganizationMapper.toEntity(name, member)))
                .collect(Collectors.toSet());

        gitOrganizationRepository.saveAll(gitOrganizations);
    }

    public List<GitOrganization> findGitOrganizationByGithubId(String githubId) {
        return gitOrganizationRepository.findByGithubId(githubId);
    }
}
