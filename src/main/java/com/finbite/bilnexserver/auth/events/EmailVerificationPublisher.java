package com.finbite.bilnexserver.auth.events;

import com.finbite.bilnexserver.common.events.EmailVerificationRequest;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EmailVerificationPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void sendVerificationRequest(String email) {
        String verificationCode = generateVerificationCode();
        applicationEventPublisher.publishEvent(new EmailVerificationRequest(this, email, verificationCode));
    }

    // PRIVATE METHODS //
    private String generateVerificationCode() {
        int code = UUID.randomUUID().hashCode() % 1000000;
        return String.format("%06d", (code < 0) ? -code : code);
    }
}
