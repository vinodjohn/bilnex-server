package com.finbite.bilnexserver.auth.dtos;

import com.finbite.bilnexserver.auth.models.SystemLanguage;

import java.util.List;
import java.util.UUID;

/**
 * Person DTO
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public record PersonDto(UUID id, String email, String role, List<CompanyDto> companyDtoList, boolean isVerified,
                        boolean isGoogleUser, SystemLanguage defaultLanguage, boolean isActive) {
}
