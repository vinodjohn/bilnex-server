package com.finbite.bilnexserver.auth.dtos;

import com.finbite.bilnexserver.auth.utils.constraints.ValidSignup;

import java.util.UUID;

/**
 * Sign up request DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@ValidSignup
public record SignUp(UUID id, String email, String password, boolean isVerified, CompanyDto company) {
}
