package com.dragonguard.backend.organization.controller;

import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.organization.entity.OrganizationType;
import com.dragonguard.backend.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 김승진
 * @description 조직(회사, 대학교)관련 요청을 받아오는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<IdResponse<Long>> postOrganization(@RequestBody OrganizationRequest organizationRequest) {
        return ResponseEntity.ok(organizationService.saveOrganization(organizationRequest));
    }

    @PostMapping("/add-member")
    public ResponseEntity<Void> addMemberToOrganization(@RequestBody AddMemberRequest addMemberRequest) {
        organizationService.findAndAddMember(addMemberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<OrganizationType>> getTypes() {
        return ResponseEntity.ok(organizationService.getTypes());
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getOrganizations(@RequestParam OrganizationType type) {
        return ResponseEntity.ok(organizationService.findByType(type));
    }
}
