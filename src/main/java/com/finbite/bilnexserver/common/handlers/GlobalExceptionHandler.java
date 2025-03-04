package com.finbite.bilnexserver.common.handlers;

import com.finbite.bilnexserver.common.exceptions.AppValidationException;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationException;
import com.finbite.bilnexserver.common.exceptions.EmailVerificationNotFoundException;
import com.finbite.bilnexserver.common.models.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.finbite.bilnexserver.common.utils.AppUtils.getErrorResponse;

/**
 * Handler for exceptions
 *
 * @author vinodjohn
 * @created 01.03.2025
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error!", details);
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(AppValidationException.class)
    public final ResponseEntity<Object> handleLoanValidationException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), "LoanValidationException"),
                ex.getLocalizedMessage().contains("Invalid") ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND);
    }

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse("Validation Failed!", details);
        return ResponseEntity.badRequest().body(error);
    }
}
