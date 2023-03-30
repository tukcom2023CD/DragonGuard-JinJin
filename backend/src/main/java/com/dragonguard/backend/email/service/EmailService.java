package com.dragonguard.backend.email.service;

import com.dragonguard.backend.email.dto.request.EmailRequest;
import com.dragonguard.backend.email.dto.response.CheckCodeResponse;
import com.dragonguard.backend.email.entity.Email;
import com.dragonguard.backend.email.exception.EmailException;
import com.dragonguard.backend.email.repository.EmailRepository;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final AuthService authService;
    private final MemberService memberService;
    private Integer min = 10000;
    private Integer max = 99999;

    @Transactional
    public IdResponse<Long> sendEmail() {
        Member member = authService.getLoginUser();
        String memberEmail = member.getOrganizationDetails().getOrganizationEmail();

        if (StringUtils.hasText(memberEmail)) {
            throw new EmailException();
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        int random = new Random().nextInt(max - min) + min;
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(memberEmail);
            mimeMessageHelper.setSubject("GitRank 조직 인증");
            mimeMessageHelper.setText(getEmailText(random), true);
            javaMailSender.send(mimeMessage);

            Email email = Email.builder()
                    .code(random)
                    .memberId(member.getId())
                    .build();

            Email entity = emailRepository.save(email);

            return new IdResponse<>(entity.getId());
        } catch (MessagingException e) {
            throw new EmailException();
        }
    }

    @Transactional
    public void deleteCode(Long id) {
        getEntity(id).delete();
    }

    @Transactional
    public CheckCodeResponse isCodeMatching(EmailRequest emailRequest) {
        Long id = emailRequest.getId();
        boolean flag = getEntity(id).getCode().equals(emailRequest.getCode());

        if (!flag) return new CheckCodeResponse(false);

        deleteCode(id);
        memberService.getEntity(authService.getLoginUser().getId()).finishAuth();

        return new CheckCodeResponse(true);
    }

    private Email getEntity(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private String getEmailText(Integer code) {
        return "<html><head></head><body><div>다음 번호를 입력해주세요:\n<div><h1>" + code + "</h1></body></html>";
    }
}
