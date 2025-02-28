package com.finbite.bilnexserver.auth.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Sign in Request DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public record SignIn(@NotBlank(message = "{messages.constraints.invalid-email}") String email,
                     @NotBlank(message = "{messages.constraints.invalid-password}") String password) {
}