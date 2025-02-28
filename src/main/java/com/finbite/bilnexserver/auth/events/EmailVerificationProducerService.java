package com.finbite.bilnexserver.auth.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.finbite.bilnexserver.common.utils.Constants.Events.EMAIL_VERIFICATION;

/**
 * Service to handle Email Verification Producer operations
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Service
public class EmailVerificationProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendVerificationRequest(String email) {
        String verificationCode = generateVerificationCode();
        String eventMessage = "email=" + email + ",verificationCode=" + verificationCode;
        kafkaTemplate.send(EMAIL_VERIFICATION, eventMessage);
    }

    private String generateVerificationCode() {
        return String.format("%06d", UUID.randomUUID().hashCode() % 1000000);
    }
}
