package com.dragonguard.backend.member.messagequeue;

import com.dragonguard.backend.member.entity.AuthStep;
import com.dragonguard.backend.member.entity.Role;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaContributionConsumer {
    private final MemberService memberService;

    @KafkaListener(topics = "gitrank.to.backend.contribution", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        memberService.scrapeAndGetSavedMember(message, Role.ROLE_USER, AuthStep.GITHUB_ONLY);
    }
}
