package com.finbite.bilnexserver.common.dtos;

import java.util.UUID;

/**
 * User DTO
 *
 * @author vinodjohn
 * @created 28.02.2025
 */
public record UserDto(UUID id, String email, String password, String role) {
}
