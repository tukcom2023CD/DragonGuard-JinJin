package com.dragonguard.backend.domain.admin.service;

import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import com.dragonguard.backend.support.fixture.organization.entity.OrganizationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
@DisplayName("admin 서비스의")
class AdminServiceTest extends LoginTest {

    @Autowired private AdminService adminService;
    @Autowired private OrganizationRepository organizationRepository;

    @Test
    @DisplayName("승인 요청온 조직에 대해 승인/반려")
    void decideRequestedOrganization() {
        //given
        Organization organization = organizationRepository.save(OrganizationFixture.SAMPLE1.toEntity());

        //when
        adminService.decideRequestedOrganization(new AdminDecideRequest(organization.getId(), OrganizationStatus.ACCEPTED));

        //then
        assertThat(organization.getOrganizationStatus()).isEqualTo(OrganizationStatus.ACCEPTED);
    }

    @Test
    @DisplayName("승인/반려/요청 상태별 조직 조회")
    void getOrganizationsByStatus() {
        //given
        Organization organization1 = organizationRepository.save(OrganizationFixture.SAMPLE1.toEntity());
        Organization organization2 = organizationRepository.save(OrganizationFixture.SAMPLE2.toEntity());
        Organization organization3 = organizationRepository.save(OrganizationFixture.SAMPLE3.toEntity());

        adminService.decideRequestedOrganization(new AdminDecideRequest(organization1.getId(), OrganizationStatus.ACCEPTED));
        adminService.decideRequestedOrganization(new AdminDecideRequest(organization2.getId(), OrganizationStatus.ACCEPTED));
        adminService.decideRequestedOrganization(new AdminDecideRequest(organization3.getId(), OrganizationStatus.DENIED));

        //when
        List<AdminOrganizationResponse> acceptedList = adminService.getOrganizationsByStatus(OrganizationStatus.ACCEPTED, PageRequest.of(0, 20));
        List<AdminOrganizationResponse> deniedList = adminService.getOrganizationsByStatus(OrganizationStatus.DENIED, PageRequest.of(0, 20));

        //then
        assertThat(acceptedList).hasSize(2);
        assertThat(deniedList).hasSize(1);

        assertThat(List.of(acceptedList.get(0).getId(), acceptedList.get(1).getId())).containsExactlyInAnyOrder(organization1.getId(), organization2.getId());
        assertThat(deniedList.get(0).getId()).isEqualTo(organization3.getId());
    }
}
