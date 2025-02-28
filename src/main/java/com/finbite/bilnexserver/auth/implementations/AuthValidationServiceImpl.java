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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Implementation of AuthValidationService
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Service
@Slf4j
public class AuthValidationServiceImpl implements AuthValidationService {
    @Autowired
    private PersonService personService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void validatePerson(Person person) {
        if (person.getId() == null && !person.isActive() && !person.isVerified()) {
            try {
                personService.findPersonByEmail(person.getEmail());
                throw new AppValidationException(MessageFormat.format("{0} {1} already exists!", "Person", "Email"));
            } catch (PersonNotFoundException | AppValidationException e) {
                log.info("Person with email {} not found", person.getEmail());
            }
        }

        if (!person.isActive() && person.isVerified() && !person.getPassword().isEmpty() && !isPasswordValid(person.getPassword())) {
            log.info("Password validation failed for email {}.", person.getEmail());
            throw new RuntimeException(MessageFormat.format("{0} is not a valid password!", person.getPassword()));
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
    public void validateCompany(Company company) {
        if (company.getName().isEmpty() || company.getRegCode().isEmpty() || company.getAddress().isEmpty()
                || company.getCity().isEmpty()) {
            throw new RuntimeException("Company details missing!");
        }
    }

    // PRIVATE METHODS //
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
}
