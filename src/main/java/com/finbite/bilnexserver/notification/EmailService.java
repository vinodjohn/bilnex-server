package com.finbite.bilnexserver.notification;

/**
 * Service for email related operations
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
public interface EmailService {
    /**
     * To send an email
     *
     * @param email Email id of the receiver
     * @param subject Subject of the email
     * @param body Body of the email
     */
    void sendEmail(String email, String subject, String body);
}
