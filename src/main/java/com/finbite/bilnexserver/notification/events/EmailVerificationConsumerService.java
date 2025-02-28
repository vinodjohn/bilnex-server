package com.finbite.bilnexserver.notification.events;

import com.finbite.bilnexserver.notification.EmailVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.finbite.bilnexserver.common.utils.Constants.Events.EMAIL_VERIFICATION;

/**
 * Service to handle Email Verification Consumer operations
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Slf4j
@Service
public class EmailVerificationConsumerService {
    @Autowired

    private EmailVerificationService emailVerificationService;

    @KafkaListener(topics = EMAIL_VERIFICATION, groupId = "bilnex-events-group")
    public void listenToEmailVerificationEvent(String eventMessage) {
        try {
            String[] parts = eventMessage.split(",");
            String email = parts[0].split("=")[1];
            String verificationCode = parts[1].split("=")[1];

            emailVerificationService.sendVerificationEmail(email, verificationCode);
        } catch (Exception e) {
            log.error("Error processing email verification event: {}", e.getMessage());
        }
    }
}
