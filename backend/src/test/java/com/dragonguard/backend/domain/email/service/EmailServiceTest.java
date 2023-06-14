package com.dragonguard.backend.domain.email.service;

import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.domain.email.repository.EmailRepository;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import com.dragonguard.backend.support.fixture.organization.entity.OrganizationFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DatabaseTest
@DisplayName("email 서비스의")
class EmailServiceTest extends LoginTest {

    @Autowired private EmailService emailService;
    @Autowired private EmailRepository emailRepository;
    @Autowired private EntityManager em;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private MemberQueryRepository memberQueryRepository;

    @Test
    @DisplayName("이메일 전송")
    void sendEmail() {
        //given
        Organization organization = organizationRepository.save(OrganizationFixture.SAMPLE1.toEntity());
        organization.addMember(memberQueryRepository.findById(loginUser.getId()).get(), "ohksj77@tukorea.ac.kr");

        //when
        Long emailId = emailService.sendEmail().getId();

        //then
        assertThat(emailRepository.findById(emailId)).isNotEmpty();
    }

    @Test
    @DisplayName("이메일 코드 db 삭제")
    void deleteCode() {
        //given
        Long given = emailRepository.save(Email.builder().code(11111).memberId(loginUser.getId()).build()).getId();

        //when
        emailService.deleteCode(given);

        em.clear();

        Optional<Email> result = emailRepository.findById(given);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("이메일 코드 일치 확인")
    void isCodeMatching() {
        //given
        int code = 11111;
        Long given = emailRepository.save(Email.builder().code(code).memberId(loginUser.getId()).build()).getId();

        //when
        CheckCodeResponse falseResult = emailService.isCodeMatching(new EmailRequest(given, code + 1));
        CheckCodeResponse trueResult = emailService.isCodeMatching(new EmailRequest(given, code));

        //then
        assertThat(falseResult.isValidCode()).isFalse();
        assertThat(trueResult.isValidCode()).isTrue();
    }

    @Test
    @DisplayName("이메일 단건 조회")
    void loadEntity() {
        //given
        Email given = emailRepository.save(Email.builder().code(11111).memberId(loginUser.getId()).build());

        //when
        Email result = emailService.loadEntity(given.getId());

        //then
        assertThat(given).isEqualTo(result);
    }
}
