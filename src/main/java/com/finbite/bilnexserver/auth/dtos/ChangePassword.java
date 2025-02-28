package com.finbite.bilnexserver.auth.dtos;

import com.finbite.bilnexserver.auth.utils.constraints.ValidChangePassword;

/**
 * Change password DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@ValidChangePassword
public record ChangePassword(String oldPassword, String newPassword) {
}

