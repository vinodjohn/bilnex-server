package com.finbite.bilnexserver.auth.utils.constraints;

import com.finbite.bilnexserver.auth.AuthValidationService;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.common.exceptions.AppValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Person is valid
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Component
@AllArgsConstructor
public class PersonValidator implements ConstraintValidator<ValidPerson, Person> {
    private final AuthValidationService authValidationService;

    @Override
    public void initialize(ValidPerson constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        try {
            authValidationService.validatePerson(person);
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
