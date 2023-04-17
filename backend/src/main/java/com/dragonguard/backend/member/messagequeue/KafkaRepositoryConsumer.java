package com.dragonguard.backend.member.messagequeue;

import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.MemberClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaRepositoryConsumer {
    private final MemberClientService memberClientService;
    private final MemberRepository memberRepository;

    @KafkaListener(topics = "gitrank.to.backend.repository", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Member member = memberRepository.findMemberByGithubId(message).orElseThrow(EntityNotFoundException::new);
        memberClientService.addMemberGitRepoAndGitOrganization(member);
    }
}
