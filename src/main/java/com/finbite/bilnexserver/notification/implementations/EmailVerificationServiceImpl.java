package com.finbite.bilnexserver.notification.implementations;

import com.finbite.bilnexserver.common.exceptions.EmailVerificationException;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationNotFoundException;
import com.finbite.bilnexserver.notification.EmailService;
import com.finbite.bilnexserver.notification.EmailVerificationService;
import com.finbite.bilnexserver.notification.models.EmailVerification;
import com.finbite.bilnexserver.notification.repositories.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * Implementation of Email Verification Service
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final EmailService emailService;

    private final EmailVerificationRepository emailVerificationRepository;

    @Value("${bilnex.app.emailVerificationExpirationMinutes}")
    private long expirationMinutes;

    @Override
    public void sendVerificationEmail(EmailVerification emailVerification) {
        log.info("Handling email verification event for {}", emailVerification.getEmail());

        String subject = "Bilnex verification email";
        String body =
                "Your verification code is: " + emailVerification.getVerificationCode() + ".\nCode expires in " + expirationMinutes +
                        " minutes.";

        emailService.sendEmail(emailVerification.getEmail(), subject, body);

        log.info("Verification email sent to {}", emailVerification.getEmail());

        emailVerification.setExpirationTime(LocalDateTime.now().plusMinutes(expirationMinutes));

        emailVerificationRepository.saveAndFlush(emailVerification);
    }

    @Override
    public boolean verifyCode(String email, String code) throws EmailVerificationNotFoundException,
            EmailVerificationException {
        EmailVerification emailVerification = emailVerificationRepository
                .findTopByEmailOrderByExpirationTimeDesc(email)
                .orElseThrow(() -> new EmailVerificationNotFoundException(email));

        if (!emailVerification.getVerificationCode().equals(code)) {
            throw new EmailVerificationException(MessageFormat.format("Verification code {0} does not " +
                    "match for email: {1}!", code, email));
        }

        if (!emailVerification.getExpirationTime().isAfter(LocalDateTime.now())) {
            throw new EmailVerificationException(MessageFormat.format("Verification code {0} expired " +
                    "for email: {1}!", code, email));
        }

        return true;
    }
}
