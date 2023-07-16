package com.dragonguard.backend.domain.email.controller;

import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.organization.service.OrganizationEmailFacade;
import com.dragonguard.backend.global.dto.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 김승진
 * @description 이메일 관련 요청을 받아와 처리하는 Controller 클래스
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final OrganizationEmailFacade organizationEmailFacade;

    /**
     * 조직 인증 이메일 재전송 api
     */
    @PostMapping("/send")
    public ResponseEntity<IdResponse<Long>> sendEmail() {
        return ResponseEntity.ok(organizationEmailFacade.sendAndSaveEmail());
    }

    /**
     * 조직 인증 이메일의 인증 번호 검증 및 검증 성공 시 회원을 조직에 추가해주는 api
     */
    @GetMapping("/check")
    public ResponseEntity<CheckCodeResponse> checkCode(@Valid EmailRequest emailRequest) {
        return ResponseEntity.ok(organizationEmailFacade.isCodeMatching(emailRequest));
    }

    /**
     * 인증 시간이 지날 때까지 인증에 실패한 경우 코드 삭제 api
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        organizationEmailFacade.deleteCode(id);
        return ResponseEntity.ok().build();
    }
}
