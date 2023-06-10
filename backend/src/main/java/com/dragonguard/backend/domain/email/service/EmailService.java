package com.dragonguard.backend.domain.email.service;

import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.domain.email.exception.EmailException;
import com.dragonguard.backend.domain.email.mapper.EmailMapper;
import com.dragonguard.backend.domain.email.repository.EmailRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@TransactionService
@RequiredArgsConstructor
public class EmailService implements EntityLoader<Email, Long> {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final MemberService memberService;
    private final EmailMapper emailMapper;
    private static final String emailSubject = "GitRank 조직 인증";
    private static final int MIN = 10000;
    private static final int MAX = 99999;

    public IdResponse<Long> sendEmail() {
        Member member = memberService.getLoginUserWithPersistence();
        String memberEmail = member.getEmailAddress();

        if (!StringUtils.hasText(memberEmail)) throw new EmailException();

        int random = new Random().nextInt(MAX - MIN) + MIN;

        sendEmail(memberEmail, random);

        Email savedEmail = emailRepository.save(emailMapper.toEntity(random, member.getId()));

        return new IdResponse<>(savedEmail.getId());
    }

    public void deleteCode(final Long id) {
        loadEntity(id).delete();
    }

    public CheckCodeResponse isCodeMatching(final EmailRequest emailRequest) {
        Long id = emailRequest.getId();
        boolean flag = loadEntity(id).getCode().equals(emailRequest.getCode());

        if (!flag) return new CheckCodeResponse(false);

        deleteCode(id);
        memberService.getLoginUserWithPersistence().finishAuth();

        return new CheckCodeResponse(true);
    }

    @Override
    public Email loadEntity(final Long id) {
        return emailRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void sendEmail(String memberEmail, final int random) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(memberEmail);
            mimeMessageHelper.setSubject(emailSubject);
            mimeMessageHelper.setText(getEmailText(random), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {}
    }

    private String getEmailText(final Integer code) {
        return "<html><head></head><body><div>다음 번호를 입력해주세요:\n<div><h1>" + code + "</h1></body></html>";
    }
}
