package com.finbite.bilnexserver.notification.events;

import com.finbite.bilnexserver.common.events.EmailVerificationRequest;
import com.finbite.bilnexserver.notification.EmailVerificationService;
import com.finbite.bilnexserver.notification.models.EmailVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service to handle Email Verification Consumer operations
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Slf4j
@Service
public class EmailVerificationListener {
    @Autowired
    private EmailVerificationService emailVerificationService;

    @Async
    @EventListener
    @Retryable(backoff = @Backoff(delay = 3000)) // 3 retries as default
    public void listenToEmailVerificationEvent(EmailVerificationRequest event) {
        try {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.setEmail(event.getEmail());
            emailVerification.setVerificationCode(event.getCode());

            emailVerificationService.sendVerificationEmail(emailVerification);
        } catch (Exception e) {
            log.error("Error processing email verification event: {}", e.getMessage());
        }
    }
}
