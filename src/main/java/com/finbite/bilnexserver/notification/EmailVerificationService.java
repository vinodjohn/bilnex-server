package com.finbite.bilnexserver.notification;

/**
 * Service to handle email verification operations
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
public interface EmailVerificationService {
    /**
     * To send verification email to the user
     *
     * @param email Email of the user
     * @param verificationCode Generated verification code
     */
    void sendVerificationEmail(String email, String verificationCode);
}
