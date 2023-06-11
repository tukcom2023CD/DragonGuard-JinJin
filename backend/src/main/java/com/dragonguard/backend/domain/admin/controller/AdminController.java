package com.dragonguard.backend.domain.admin.controller;

import com.dragonguard.backend.domain.admin.dto.request.AdminDecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.AdminOrganizationResponse;
import com.dragonguard.backend.domain.admin.service.AdminService;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.global.Admin;
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

    @GetMapping("/check")
    public ResponseEntity<Void> checkAdmin() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/organizations/decide")
    public ResponseEntity<List<AdminOrganizationResponse>> decideRequest(@RequestBody @Valid AdminDecideRequest adminDecideRequest) {
        return ResponseEntity.ok(adminService.decideRequestedOrganization(adminDecideRequest));
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<AdminOrganizationResponse>> getOrganizationsByStatus(
            @RequestParam OrganizationStatus status, Pageable pageable) {
        return ResponseEntity.ok(adminService.getOrganizationsByStatus(status, pageable));
    }
}
