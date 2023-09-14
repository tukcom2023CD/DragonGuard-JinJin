package com.dragonguard.backend.domain.organization.controller;

import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.domain.organization.service.OrganizationEmailFacade;
import com.dragonguard.backend.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 김승진
 * @description 조직(회사, 대학교)관련 요청을 받아오는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {
    private final OrganizationEmailFacade organizationService;

    /**
     * 조직을 등록하기 위해 요청하는 api (이후 관리자의 승인 필요)
     */
    @PostMapping
    public ResponseEntity<IdResponse<Long>> saveOrganization(@RequestBody @Valid final OrganizationRequest organizationRequest) {
        return ResponseEntity.ok(organizationService.saveOrganization(organizationRequest));
    }

    /**
     * 조직의 이름을 통해 id를 조회하는 api
     */
    @GetMapping("/search-id")
    public ResponseEntity<IdResponse<Long>> getOrganization(@RequestParam final String name) {
        return ResponseEntity.ok(organizationService.getByName(name));
    }

    /**
     * 멤버의 조직에 추가 요청 api (이후 이메일 인증 필요)
     */
    @PostMapping("/add-member")
    public ResponseEntity<IdResponse<Long>> addMemberToOrganization(@RequestBody @Valid final AddMemberRequest addMemberRequest) {
        return ResponseEntity.ok(organizationService.addMemberAndSendEmail(addMemberRequest));
    }

    /**
     * 조직 타입별 조회 api
     */
    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getOrganizations(@RequestParam final OrganizationType type, final Pageable pageable) {
        return ResponseEntity.ok(organizationService.findByType(type, pageable));
    }

    /**
     * 조직 자체의 랭킹을 페이징을 통해 조회하는 api
     */
    @GetMapping("/ranking/all")
    public ResponseEntity<List<OrganizationResponse>> getOrganizationRank(final Pageable pageable) {
        return ResponseEntity.ok(organizationService.getOrganizationRank(pageable));
    }

    /**
     * 조직의 타입별 랭킹을 페이징을 통해 조회하는 api
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<OrganizationResponse>> getOrganizationRankByType(
            @RequestParam final OrganizationType type, final Pageable pageable) {
        return ResponseEntity.ok(organizationService.getOrganizationRankByType(type, pageable));
    }

    /**
     * 조직을 이름으로 검색하는 api
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrganizationResponse>> searchOrganization(
            @RequestParam final OrganizationType type, @RequestParam final String name, final Pageable pageable) {
        return ResponseEntity.ok(organizationService.searchOrganization(type, name, pageable));
    }
}
