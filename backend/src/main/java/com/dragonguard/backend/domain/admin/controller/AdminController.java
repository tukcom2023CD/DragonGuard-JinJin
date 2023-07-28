package com.dragonguard.backend.domain.admin.controller;

import com.dragonguard.backend.domain.admin.annotation.Admin;
import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.admin.service.AdminService;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 김승진
 * @description 관리자 기능을 담당하는 controller
 */

@Admin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    /**
     * 관리자 권한을 체크하는 api (화면을 다르게 보여주기 위함)
     */
    @GetMapping("/check")
    public ResponseEntity<Void> checkAdmin() {
        return ResponseEntity.ok().build();
    }

    /**
     * 조직 승인 요청에 대해 관리자의 선택에 대한 요청을 받는 api
     */
    @PostMapping("/organizations/decide")
    public ResponseEntity<List<AdminOrganizationResponse>> decideRequest(@RequestBody @Valid AdminDecideRequest adminDecideRequest) {
        return ResponseEntity.ok(adminService.decideRequestedOrganization(adminDecideRequest));
    }

    /**
     * 조직의 승인 여부에 따른 조회
     */
    @GetMapping("/organizations")
    public ResponseEntity<List<AdminOrganizationResponse>> getOrganizationsByStatus(
            @RequestParam OrganizationStatus status, Pageable pageable) {
        return ResponseEntity.ok(adminService.getOrganizationsByStatus(status, pageable));
    }
}
