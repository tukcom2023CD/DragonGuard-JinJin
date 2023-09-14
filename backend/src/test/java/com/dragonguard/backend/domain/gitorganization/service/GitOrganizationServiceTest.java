package com.dragonguard.backend.domain.gitorganization.service;

import com.dragonguard.backend.domain.gitorganization.entity.GitOrganization;
import com.dragonguard.backend.domain.gitorganization.repository.GitOrganizationRepository;
import com.dragonguard.backend.domain.gitorganization.repository.JpaGitOrganizationRepository;
import com.dragonguard.backend.domain.member.dto.client.MemberOrganizationResponse;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GitOrganization 서비스의")
class GitOrganizationServiceTest extends LoginTest {
    @Autowired private GitOrganizationService gitOrganizationService;
    @Autowired private GitOrganizationRepository gitOrganizationRepository;
    @Autowired private JpaGitOrganizationRepository jpaGitOrganizationRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("전체 저장이 수행되는가")
    void saveGitOrganizations() {
        //given
        Set<MemberOrganizationResponse> gitOrganizationNames = Set.of(
                new MemberOrganizationResponse("tukcom2023CD", "http://githubProfileImage1"),
                new MemberOrganizationResponse("C-B-U", "http://githubProfileImage2"));

        //when
        gitOrganizationService.findAndSaveGitOrganizations(gitOrganizationNames, memberRepository.findById(authService.getLoginUserId()).orElseThrow());

        long count = gitOrganizationNames.stream()
                .map(MemberOrganizationResponse::getLogin)
                .map(gitOrganizationRepository::findByName)
                .map(go -> go.orElse(null))
                .filter(Objects::nonNull)
                .count();

        //then
        assertThat(count).isEqualTo(Long.valueOf(gitOrganizationNames.size()));
    }

    @Test
    @DisplayName("유저의 깃허브 id로 조회가 수행되는가")
    void findGitOrganizationByGithubId() {
        //given
        List<GitOrganization> given = List.of(GitOrganization.builder().member(loginUser).name("tukcom2023CD").build(),
                GitOrganization.builder().member(loginUser).name("C-B-U").build(),
                GitOrganization.builder().member(loginUser).name("bid-bid").build());

        gitOrganizationRepository.saveAll(given);

        //when
        List<GitOrganization> gitOrganization = gitOrganizationService.findGitOrganizationByMember(memberRepository.findById(loginUser.getId()).orElse(null));

        //then
        assertThat(gitOrganization).hasSameElementsAs(given);
    }

    @Test
    @DisplayName("깃허브 조직 id로 조회가 수행되는가")
    void loadEntity() {
        //given
        GitOrganization given = jpaGitOrganizationRepository.save(GitOrganization.builder().name("tukcom2023CD").member(loginUser).build());

        //when
        GitOrganization result = gitOrganizationService.loadEntity(given.getId());

        //then
        assertThat(given).isEqualTo(result);
    }
}
