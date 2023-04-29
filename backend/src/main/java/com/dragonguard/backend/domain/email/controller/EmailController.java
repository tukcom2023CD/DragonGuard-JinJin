package com.dragonguard.backend.domain.email.controller;

import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.global.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<IdResponse<Long>> sendEmail() {
        return ResponseEntity.ok(emailService.sendEmail());
    }

    @GetMapping("/check")
    public ResponseEntity<CheckCodeResponse> checkCode(@Valid EmailRequest emailRequest) {
        return ResponseEntity.ok(emailService.isCodeMatching(emailRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        emailService.deleteCode(id);
        return ResponseEntity.ok().build();
    }
}
