package com.finbite.bilnexserver.auth.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Company's unavailability
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
public class CompanyNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public CompanyNotFoundException(UUID id) {
        super(MessageFormat.format("Company not found! (ID: {0})", id));
    }
}
