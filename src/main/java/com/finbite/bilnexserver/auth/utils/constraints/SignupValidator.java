package com.finbite.bilnexserver.auth.utils.constraints;

import com.finbite.bilnexserver.auth.AuthValidationService;
import com.finbite.bilnexserver.auth.dtos.SignUp;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.models.Person;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Constraint validator to check if Signup is valid
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public class SignupValidator implements ConstraintValidator<ValidSignup, SignUp> {
    @Autowired
    private AuthValidationService authValidationService;

    @Override
    public void initialize(ValidSignup constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUp signup, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Person person = new Person();
            person.setEmail(signup.email());
            person.setPassword(signup.password());
            person.setVerified(signup.isVerified());

            authValidationService.validatePerson(person);

            if (signup.isVerified() && signup.company().id() == null) {
                Company company = new Company();
                company.setName(signup.company().name());
                company.setRegCode(signup.company().regCode());
                company.setVatNr(signup.company().vatNr());
                company.setAddress(signup.company().address());
                company.setCity(signup.company().city());
                company.setZipcode(signup.company().zipcode());

                authValidationService.validateCompany(company);
            }

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
