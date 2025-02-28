package com.finbite.bilnexserver.auth.utils.constraints;

import com.finbite.bilnexserver.auth.AuthValidationService;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Person;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Constraint validator to check if Person is valid
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Component
public class PersonValidator implements ConstraintValidator<ValidPerson, Person> {
    @Autowired
    private AuthValidationService authValidationService;

    @Override
    public void initialize(ValidPerson constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        try {
            authValidationService.validatePerson(person);
            return true;
        } catch (PersonNotFoundException e) {
            if (!e.getMessage().isBlank()) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }

        return false;
    }
}
