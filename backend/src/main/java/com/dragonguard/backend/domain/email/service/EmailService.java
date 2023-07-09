package com.dragonguard.backend.domain.email.service;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.domain.email.dto.request.EmailRequest;
import com.dragonguard.backend.domain.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.domain.email.exception.EmailException;
import com.dragonguard.backend.domain.email.mapper.EmailMapper;
import com.dragonguard.backend.domain.email.repository.EmailRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 * @author 김승진
 * @description 이메일 로직을 처리하는 서비스 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class EmailService implements EntityLoader<Email, Long> {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final AuthService authService;
    private final EmailMapper emailMapper;
    private final KafkaProducer<KafkaEmail> kafkaEmailProducer;
    private static final String EMAIL_SUBJECT = "GitRank 조직 인증";
    private static final int MIN = 10000;
    private static final int MAX = 99999;

    public IdResponse<Long> sendAndSaveEmail() {
        Member member = authService.getLoginUser();
        String memberEmail = member.getEmailAddress();

        validateMemberEmail(memberEmail);

        int randomCode = generateRandomCode();
        requestToSendEmail(memberEmail, randomCode);

        Email savedEmail = emailRepository.save(emailMapper.toEntity(randomCode, member.getId()));
        return new IdResponse<>(savedEmail.getId());
    }

    private void validateMemberEmail(final String memberEmail) {
        if (!StringUtils.hasText(memberEmail)) throw new EmailException();
    }

    public void deleteCode(final Long id) {
        loadEntity(id).delete();
    }

    public CheckCodeResponse isCodeMatching(final EmailRequest emailRequest) {
        Long id = emailRequest.getId();

        if (!loadEntity(id).getCode().equals(emailRequest.getCode())) return new CheckCodeResponse(false);
        deleteCode(id);

        authService.getLoginUser().finishAuth();
        return new CheckCodeResponse(true);
    }

    @Override
    public Email loadEntity(final Long id) {
        return emailRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private int generateRandomCode() {
        return new Random().nextInt(MAX - MIN) + MIN;
    }

    private void requestToSendEmail(final String memberEmail, final int randomCode) {
        kafkaEmailProducer.send(new KafkaEmail(memberEmail, randomCode));
    }

    public void sendEmail(final String memberEmail, final int random) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(memberEmail);
            mimeMessageHelper.setSubject(EMAIL_SUBJECT);
            mimeMessageHelper.setText(getEmailText(random), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {}
    }

    private String getEmailText(final Integer code) {
        return "<html><head></head><body><div>다음 번호를 입력해주세요:\n<div><h1>" + code + "</h1></body></html>";
    }
}
