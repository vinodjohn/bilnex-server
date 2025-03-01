package com.finbite.bilnexserver.notification;

import com.finbite.bilnexserver.common.exceptions.EmailVerificationException;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationNotFoundException;
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

    /**
     * To verify the code which is being sent already
     *
     * @param email Person's email
     * @param code  Verification code which was sent
     * @return True or false
     */
    boolean verifyCode(String email, String code) throws EmailVerificationNotFoundException, EmailVerificationException;
}
