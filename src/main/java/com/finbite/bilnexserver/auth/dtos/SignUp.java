package com.finbite.bilnexserver.auth.dtos;

import com.finbite.bilnexserver.auth.utils.constraints.ValidSignup;

/**
 * Sign up request DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@ValidSignup
public record SignUp(String email, String code, String password, boolean isVerified, CompanyDto company) {
}
