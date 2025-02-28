package com.finbite.bilnexserver.notification;

import com.finbite.bilnexserver.notification.models.EmailVerification;

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
     * @param emailVerification Email Verification Object
     */
    void sendVerificationEmail(EmailVerification emailVerification);
}
