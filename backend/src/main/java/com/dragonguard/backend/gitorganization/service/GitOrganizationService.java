package com.dragonguard.backend.gitorganization.service;

import com.dragonguard.backend.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.gitorganization.mapper.GitOrganizationMapper;
import com.dragonguard.backend.gitorganization.repository.GitOrganizationRepository;
import com.dragonguard.backend.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void saveGitOrganizations(List<String> gitOrganizationNames, Member member) {
        List<GitOrganization> gitOrganizations = gitOrganizationNames.stream()
                .filter(name -> !gitOrganizationRepository.existsByName(name))
                .map(name -> gitOrganizationMapper.toEntity(name, member))
                .collect(Collectors.toList());

        gitOrganizationRepository.saveAll(gitOrganizations);
    }

    public List<GitOrganization> findGitOrganizationByGithubId(String githubId) {
        return gitOrganizationRepository.findByGithubId(githubId);
    }
}
