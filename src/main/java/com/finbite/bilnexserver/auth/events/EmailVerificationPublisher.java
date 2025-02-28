package com.finbite.bilnexserver.auth.events;

import com.finbite.bilnexserver.common.events.EmailVerificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to handle Email Verification Producer operations
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Service
public class EmailVerificationPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void sendVerificationRequest(String email) {
        String verificationCode = generateVerificationCode();
        applicationEventPublisher.publishEvent(new EmailVerificationRequest(this, email, verificationCode));
    }

    // PRIVATE METHODS //
    private String generateVerificationCode() {
        return String.format("%06d", UUID.randomUUID().hashCode() % 1000000);
    }
}
