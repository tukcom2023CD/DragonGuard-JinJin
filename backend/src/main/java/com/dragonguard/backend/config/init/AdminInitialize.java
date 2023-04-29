//package com.dragonguard.backend.config.init;
//
//import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
//import com.dragonguard.backend.domain.member.service.MemberClientService;
//import com.dragonguard.backend.domain.member.service.MemberService;
//import com.dragonguard.backend.util.KafkaProducer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import java.time.LocalDate;
//
//@Component
//@RequiredArgsConstructor
//@DependsOnDatabaseInitialization
//public class AdminInitialize {
//    private final MemberClientService memberClientService;
//    private final MemberService memberService;
//    private final KafkaProducer<ContributionScrapingRequest> contributionKafkaProducer;
//
//    @PostConstruct
//    @Transactional
//    public void initMember() {
//        memberService.getAll().forEach(member -> {
//            contributionKafkaProducer.send(new ContributionScrapingRequest(member.getGithubId(), LocalDate.now().getYear()));
//            memberClientService.addMemberContribution(member);
//            memberService.transactionAndUpdateTier(member);
//            memberClientService.addMemberGitRepoAndGitOrganization(member);
//        });
//    }
//}
