package com.finbite.bilnexserver.auth.utils;

import com.finbite.bilnexserver.auth.dtos.SignUp;
import com.finbite.bilnexserver.auth.models.Company;

/**
 * A helper class for Auth module
 *
 * @author vinodjohn
 * @created 01.03.2025
 */
public class AuthUtils {
    public static Company translateSignupToCompany(SignUp signUp) {
        Company company = new Company();
        company.setCountry(signUp.company().countryCode());
        company.setName(signUp.company().name());
        company.setRegCode(signUp.company().regCode());
        company.setVatNr(signUp.company().vatNr());
        company.setAddress(signUp.company().address());
        company.setCity(signUp.company().city());
        company.setZipcode(signUp.company().zipcode());
        company.setHasSubscribed(signUp.company().hasSubscribed());

        return company;
    }
}
