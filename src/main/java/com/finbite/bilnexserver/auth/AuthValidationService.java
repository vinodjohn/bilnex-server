package com.finbite.bilnexserver.auth;

import com.finbite.bilnexserver.auth.dtos.ChangePassword;
import com.finbite.bilnexserver.auth.exceptions.PersonNotFoundException;
import com.finbite.bilnexserver.auth.models.Company;
import com.finbite.bilnexserver.auth.models.Person;

/**
 * Service to validate different data
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public interface AuthValidationService {
    /**
     * To validate person
     *
     * @param person Person object
     */
    void validatePerson(Person person) throws PersonNotFoundException;

    /**
     * To validate Change Password
     *
     * @param changePassword Change Password
     */
    void validateChangePassword(ChangePassword changePassword);

    /**
     * To validate company
     *
     * @param company Company object
     */
    void validateCompany(Company company);

}
