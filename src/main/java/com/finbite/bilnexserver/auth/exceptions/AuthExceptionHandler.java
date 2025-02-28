package com.finbite.bilnexserver.auth.exceptions;

import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.common.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.finbite.bilnexserver.common.utils.AppUtils.getErrorResponse;

/**
 * Handler for auth related exceptions
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@RestControllerAdvice
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(PersonNotFoundException.class)
    public final ResponseEntity<Object> handlePersonNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), Person.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public final ResponseEntity<Object> handleCompanyNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), Company.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public final ResponseEntity<Object> handleTokenRefreshException(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(request.getDescription(false));

        ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage(), details);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
