package com.dragonguard.backend.domain.admin.controller;

import com.dragonguard.backend.domain.admin.service.AdminService;
import com.dragonguard.backend.global.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
