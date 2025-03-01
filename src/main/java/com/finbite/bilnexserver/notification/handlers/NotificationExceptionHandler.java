package com.finbite.bilnexserver.notification.handlers;

import com.finbite.bilnexserver.common.exceptions.EmailVerificationException;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.finbite.bilnexserver.common.utils.AppUtils.getErrorResponse;

/**
 * Handler for notification related exceptions
 *
 * @author vinodjohn
 * @created 01.03.2025
 */
@RestControllerAdvice
public class NotificationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EmailVerificationException.class)
    public final ResponseEntity<Object> handleEmailVerificationException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), "Email Verification"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailVerificationNotFoundException.class)
    public final ResponseEntity<Object> handleEmailVerificationNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), "Email Verification"),
                HttpStatus.NOT_FOUND);
    }
}
