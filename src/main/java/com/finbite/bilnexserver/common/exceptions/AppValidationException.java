package com.finbite.bilnexserver.common.exceptions;

/**
 * Exception to handle validation related items
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class AppValidationException extends Exception {
    public AppValidationException(String message) {
        super(message);
    }
}
