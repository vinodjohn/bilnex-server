package com.finbite.bilnexserver.notification.implementations;

import com.finbite.bilnexserver.notification.EmailService;
import com.finbite.bilnexserver.notification.EmailVerificationService;
import com.finbite.bilnexserver.notification.models.EmailVerification;
import com.finbite.bilnexserver.notification.repositories.EmailVerificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Value("${bilnex.app.emailVerificationExpirationMinutes}")
    private long expirationMinutes;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Override
    public void sendVerificationEmail(EmailVerification emailVerification) {
        log.info("Handling email verification event for {}", emailVerification.getEmail());

        String subject = "Bilnex verification email";
        String body =
                "Your verification code is: " + emailVerification.getVerificationCode() + "\nIt expires in " + expirationMinutes +
                "minutes.";

        emailService.sendEmail(emailVerification.getEmail(), subject, body);

        log.info("Verification email sent to {}", emailVerification.getEmail());

        emailVerification.setExpirationTime(LocalDateTime.now().plusMinutes(expirationMinutes));

        emailVerificationRepository.save(emailVerification);
    }
}
