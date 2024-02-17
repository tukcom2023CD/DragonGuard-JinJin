package com.dragonguard.backend.domain.email.service;

import com.dragonguard.backend.domain.email.dto.kafka.KafkaEmail;
import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.domain.email.exception.EmailException;
import com.dragonguard.backend.domain.email.mapper.EmailMapper;
import com.dragonguard.backend.domain.email.repository.EmailRepository;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.service.AuthService;
import com.dragonguard.backend.global.annotation.TransactionService;
import com.dragonguard.backend.global.dto.IdResponse;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.template.kafka.KafkaProducer;
import com.dragonguard.backend.utils.RandomCodeGenerator;

import lombok.RequiredArgsConstructor;

import org.springframework.util.StringUtils;

/**
 * @author 김승진
 * @description 이메일 로직을 처리하는 서비스 클래스
 */
@TransactionService
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final AuthService authService;
    private final EmailMapper emailMapper;
    private final KafkaProducer<KafkaEmail> kafkaEmailProducer;
    private final RandomCodeGenerator randomCodeGenerator;

    public IdResponse<Long> sendAndSaveEmail() {
        final Member member = authService.getLoginUser();
        final String memberEmail = member.getEmailAddress();

        validateMemberEmail(memberEmail);

        final int randomCode = randomCodeGenerator.generate();
        requestToSendEmail(memberEmail, randomCode);

        final Email savedEmail =
                emailRepository.save(emailMapper.toEntity(randomCode, member.getId()));
        return new IdResponse<>(savedEmail.getId());
    }

    private void validateMemberEmail(final String memberEmail) {
        if (!StringUtils.hasText(memberEmail)) {
            throw new EmailException();
        }
    }

    public void deleteCode(final Long id) {
        loadEntity(id).delete();
    }

    @Override
    public Email loadEntity(final Long id) {
        return emailRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private void requestToSendEmail(final String memberEmail, final int randomCode) {
        kafkaEmailProducer.send(new KafkaEmail(memberEmail, randomCode));
    }
}
