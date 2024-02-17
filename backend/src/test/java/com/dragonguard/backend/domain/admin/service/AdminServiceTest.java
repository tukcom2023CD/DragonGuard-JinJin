package com.dragonguard.backend.domain.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.support.database.LoginTest;
import com.dragonguard.backend.support.fixture.organization.entity.OrganizationFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DisplayName("admin 서비스의")
class AdminServiceTest extends LoginTest {

    @Autowired private AdminService adminService;
    @Autowired private OrganizationRepository organizationRepository;

    @Test
    @DisplayName("승인 요청온 조직에 대해 승인/반려가 수행되는가")
    void decideRequestedOrganization() {
        // given
        Organization organization =
                organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());

        // when
        adminService.decideRequestedOrganization(
                new AdminDecideRequest(organization.getId(), OrganizationStatus.ACCEPTED));

        // then
        assertThat(organization.getOrganizationStatus()).isEqualTo(OrganizationStatus.ACCEPTED);
    }

    @Test
    @DisplayName("승인/반려/요청 상태별 조직 조회가 수행되는가")
    void getOrganizationsByStatus() {
        // given
        Organization organization1 =
                organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());
        Organization organization2 =
                organizationRepository.save(OrganizationFixture.GOOGLE.toEntity());
        Organization organization3 =
                organizationRepository.save(OrganizationFixture.SNU.toEntity());

        adminService.decideRequestedOrganization(
                new AdminDecideRequest(organization1.getId(), OrganizationStatus.ACCEPTED));
        adminService.decideRequestedOrganization(
                new AdminDecideRequest(organization2.getId(), OrganizationStatus.ACCEPTED));

        // when
        List<AdminOrganizationResponse> acceptedList =
                adminService.findOrganizationsByStatus(
                        OrganizationStatus.ACCEPTED, PageRequest.of(0, 20));

        // then
        assertThat(acceptedList).hasSize(2);

        assertThat(List.of(acceptedList.get(0).getId(), acceptedList.get(1).getId()))
                .containsExactlyInAnyOrder(organization1.getId(), organization2.getId());
    }
}
