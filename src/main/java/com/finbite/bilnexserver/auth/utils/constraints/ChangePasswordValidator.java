package com.finbite.bilnexserver.auth.utils.constraints;

import com.finbite.bilnexserver.auth.AuthValidationService;
import com.finbite.bilnexserver.auth.dtos.ChangePassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Change Password is valid
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Component
public class ChangePasswordValidator implements ConstraintValidator<ValidChangePassword, ChangePassword> {
    @Autowired
    private AuthValidationService authValidationService;

    @Override
    public void initialize(ValidChangePassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ChangePassword changePassword, ConstraintValidatorContext constraintValidatorContext) {
        try {
            authValidationService.validateChangePassword(changePassword);
            return true;
        } catch (RuntimeException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}
