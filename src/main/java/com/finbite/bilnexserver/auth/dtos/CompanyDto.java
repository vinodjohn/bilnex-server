package com.finbite.bilnexserver.auth.dtos;

import java.util.UUID;

/**
 * Company DTO>
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
public record CompanyDto(UUID id, String name, String regCode, String vatNr, String address, String city,
                         String zipcode, String country, boolean hasSubscribed, boolean isDefault, boolean isActive) {
}
