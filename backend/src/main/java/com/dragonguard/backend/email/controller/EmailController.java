package com.dragonguard.backend.email.controller;

import com.dragonguard.backend.email.dto.request.EmailRequest;
import com.dragonguard.backend.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send/{id}")
    public ResponseEntity<Void> sendEmail(@PathVariable UUID id) {
        emailService.sendEmail(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<CheckCodeResponse> checkCode(EmailRequest emailRequest) {
        return ResponseEntity.ok(emailService.isCodeMatching(emailRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        emailService.deleteCode(id);
        return ResponseEntity.ok().build();
    }
}
