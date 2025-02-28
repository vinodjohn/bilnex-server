package com.finbite.bilnexserver.auth.dtos;

import java.util.UUID;

/**
 * Person DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public record PersonDto(UUID id, String email, String role) {
}
