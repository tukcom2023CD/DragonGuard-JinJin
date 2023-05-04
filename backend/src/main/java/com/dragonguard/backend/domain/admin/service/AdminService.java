package com.dragonguard.backend.domain.admin.service;

import com.dragonguard.backend.domain.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final OrganizationService organizationService;
}
