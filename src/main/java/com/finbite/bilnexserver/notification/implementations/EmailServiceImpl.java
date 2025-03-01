package com.finbite.bilnexserver.notification.implementations;

import com.finbite.bilnexserver.notification.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementations of EmailService
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String email, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending  email: {}", e.getMessage());
            throw new RuntimeException("Technical Error! Cannot send email.");
        }
    }
}
