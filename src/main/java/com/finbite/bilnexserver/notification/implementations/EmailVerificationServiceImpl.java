package com.finbite.bilnexserver.notification.implementations;

import com.finbite.bilnexserver.notification.EmailService;
import com.finbite.bilnexserver.notification.EmailVerificationService;
import com.finbite.bilnexserver.notification.models.EmailVerification;
import com.finbite.bilnexserver.notification.repositories.EmailVerificationRepository;
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
    public void sendVerificationEmail(String email, String verificationCode) {
        String subject = "Bilnex verification email";
        String body = "Your verification code is: " + verificationCode + "\nIt expires in " + expirationMinutes + "minutes.";
        emailService.sendEmail(email, subject, body);

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(email);
        emailVerification.setVerificationCode(verificationCode);
        emailVerification.setExpirationTime(LocalDateTime.now().plusMinutes(expirationMinutes));

        emailVerificationRepository.save(emailVerification);
    }
}
