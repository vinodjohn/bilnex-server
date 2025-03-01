package com.finbite.bilnexserver.common.exceptions;

/**
 * Exception to handle Email Verification related errors
 *
 * @author vinodjohn
 * @created 01.03.2025
 */
public class EmailVerificationException extends Exception {
    public EmailVerificationException(String message) {
        super(message);
    }
}
