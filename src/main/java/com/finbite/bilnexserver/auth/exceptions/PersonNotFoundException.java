package com.finbite.bilnexserver.auth.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Exception to handle Person's unavailability
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class PersonNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public PersonNotFoundException(UUID id) {
        super(MessageFormat.format("Person not found! (ID: {0})", id));
    }

    public PersonNotFoundException(String email) {
        super(MessageFormat.format("Person not found! (Email: {0})", email));
    }
}