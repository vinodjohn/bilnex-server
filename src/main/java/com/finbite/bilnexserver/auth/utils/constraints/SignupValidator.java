package com.finbite.bilnexserver.auth.utils.constraints;

import com.finbite.bilnexserver.auth.AuthValidationService;
import com.finbite.bilnexserver.auth.dtos.SignUp;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.auth.utils.AuthUtils;
import com.finbite.bilnexserver.common.exceptions.AppValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Signup is valid
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Component
@AllArgsConstructor
public class SignupValidator implements ConstraintValidator<ValidSignup, SignUp> {
    private final AuthValidationService authValidationService;

    @Override
    public void initialize(ValidSignup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUp signUp, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (!signUp.isVerified()) {
                Person person = new Person();
                person.setEmail(signUp.email());

                authValidationService.validatePerson(person);
            } else {
                Person person = new Person();
                person.setEmail(signUp.email());
                person.setPassword(signUp.password());
                authValidationService.validatePerson(person);

                authValidationService.validateCompany(AuthUtils.translateSignupToCompany(signUp));
            }

            return true;
        } catch (PersonNotFoundException | AppValidationException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}
