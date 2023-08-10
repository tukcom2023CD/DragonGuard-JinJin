package com.dragonguard.backend.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private static final String EMAIL_SUBJECT = "GitRank 조직 인증";
    private final JavaMailSender javaMailSender;

    public void send(final String memberEmail, final int random) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(memberEmail);
            mimeMessageHelper.setSubject(EMAIL_SUBJECT);
            mimeMessageHelper.setText(getEmailText(random), Boolean.TRUE);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {}
    }

    private String getEmailText(final int code) {
        return String.format("<html><head></head><body><div>다음 번호를 입력해주세요:\n<div><h1>%d</h1></body></html>", code);
    }
}
