package com.dragonguard.backend.domain.admin.controller;

import com.dragonguard.backend.domain.admin.dto.request.DecideRequest;
import com.dragonguard.backend.domain.admin.dto.response.OrganizationAdminResponse;
import com.dragonguard.backend.domain.admin.service.AdminService;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.global.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/decide")
    public ResponseEntity<List<OrganizationAdminResponse>> decideRequest(@RequestBody DecideRequest decideRequest) {
        return ResponseEntity.ok(adminService.decideRequestedOrganization(decideRequest));
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationAdminResponse>> getOrganizationsByStatus(
            @RequestParam OrganizationStatus status, Pageable pageable) {
        return ResponseEntity.ok(adminService.getOrganizationsByStatus(status, pageable));
    }
}
