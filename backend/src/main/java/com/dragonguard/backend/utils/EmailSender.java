package com.dragonguard.backend.utils;

import com.dragonguard.backend.domain.deadletter.entity.DeadLetter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 이메일 전송에 사용될 util 클래스
 */

@Component
@RequiredArgsConstructor
public class EmailSender {
    private static final String EMAIL_SUBJECT = "GitRank 조직 인증";
    private static final String EMAIL_CONTENT = "<html><head></head><body><div>다음 번호를 입력해주세요:\n<div><h1>%d</h1></body></html>";
    private static final String DEAD_LETTER_SUBJECT = "GitRank 데드레터 발생";
    private static final String DEAD_LETTER_CONTENT = "<html><head></head><body><h1>데드레터 목록</h1><div>%s</div></body></html>";

    @Value("${admin-email}")
    private String adminEmail;

    private final JavaMailSender javaMailSender;

    public void send(final String memberEmail, final int random) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(memberEmail);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText(getEmailText(random), Boolean.TRUE);
        javaMailSender.send(mimeMessage);
    }

    private String getEmailText(final int code) {
        return String.format(EMAIL_CONTENT, code);
    }

    public void sendDeadLetter(final DeadLetter deadLetter) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(adminEmail);
        mimeMessageHelper.setSubject(DEAD_LETTER_SUBJECT);
        mimeMessageHelper.setText(getDeadLetterText(deadLetter), Boolean.TRUE);
        javaMailSender.send(mimeMessage);
    }

    private String getDeadLetterText(final DeadLetter deadLetter) {
        final String content = "topic-name" + deadLetter.getTopicName()
                + "key-name" + deadLetter.getKeyName()
                + "time" + deadLetter.getBaseTime().getCreatedAt()
                + "payload" + deadLetter.getValueObject();

        return String.format(DEAD_LETTER_CONTENT, content);
    }
}
