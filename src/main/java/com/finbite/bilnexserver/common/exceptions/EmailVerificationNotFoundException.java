package com.finbite.bilnexserver.common.exceptions;

/**
 * Exception to handle Email Verification unavailability
 *
 * @author vinodjohn
 * @created 01.03.2025
 */
public class EmailVerificationNotFoundException extends Exception {
    public EmailVerificationNotFoundException(String email) {
        super("No email verification token found for email: " + email);
    }
}
