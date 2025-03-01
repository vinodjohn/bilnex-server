package com.finbite.bilnexserver.auth.implementations;

import com.finbite.bilnexserver.auth.AuthValidationService;
import com.finbite.bilnexserver.auth.PersonService;
import com.finbite.bilnexserver.auth.configs.CustomUserDetails;
import com.finbite.bilnexserver.auth.dtos.ChangePassword;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.models.Person;
import com.finbite.bilnexserver.common.dtos.UserDto;
import com.finbite.bilnexserver.common.exceptions.AppValidationException;
import com.finbite.bilnexserver.common.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Implementation of AuthValidationService
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Service
@Slf4j
@AllArgsConstructor
public class AuthValidationServiceImpl implements AuthValidationService {
    private final PersonService personService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void validatePerson(Person person) throws AppValidationException {
        if (person.getId() == null && !person.isActive()) {
            if (!isEmailValid(person.getEmail())) {
                throw new AppValidationException(AppUtils.getExceptionMessage("Person", "email", false));
            }

            try {
                personService.findPersonByEmail(person.getEmail());
                throw new AppValidationException(AppUtils.getExceptionMessage("Person", "email", true));
            } catch (PersonNotFoundException e) {
                log.info("Person with email {} not found", person.getEmail());
            }
        }

        if (!person.isActive() && person.isVerified() && !person.getPassword().isEmpty() && !isPasswordValid(person.getPassword())) {
            log.info("Password validation failed for email {}.", person.getEmail());
            throw new AppValidationException(AppUtils.getExceptionMessage("Person", "password", false));
        }
    }

    @Override
    public void validateChangePassword(ChangePassword changePassword) {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDto userDto = customUserDetails.getUserDto();

        if (!bCryptPasswordEncoder.matches(changePassword.oldPassword(), userDto.password())) {
            throw new RuntimeException("Old Password is incorrect!");
        }

        if (isPasswordValid(changePassword.newPassword())) {
            throw new RuntimeException("New Password is invalid!");
        }
    }

    @Override
    public void validateCompany(Company company) throws AppValidationException {
        if (company.getCountry().isEmpty()) {
            throw new AppValidationException(AppUtils.getExceptionMessage("Company", "country", false));
        }

        if (company.getName().isEmpty()) {
            throw new AppValidationException(AppUtils.getExceptionMessage("Company", "name", false));
        }

        if (company.getRegCode().isEmpty()) {
            throw new AppValidationException(AppUtils.getExceptionMessage("Company", "Reg code", false));
        }

        if (company.getAddress().isEmpty()) {
            throw new AppValidationException(AppUtils.getExceptionMessage("Company", "Address", false));
        }

        if (company.getCity().isEmpty()) {
            throw new AppValidationException(AppUtils.getExceptionMessage("Company", "city", false));
        }
    }

    // PRIVATE METHODS //
    private boolean isEmailValid(String email) {
        return Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$").matcher(password).matches();
    }
}
