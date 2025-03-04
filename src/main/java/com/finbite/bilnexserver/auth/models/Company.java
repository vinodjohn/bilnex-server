package com.finbite.bilnexserver.auth.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finbite.bilnexserver.auth.dtos.CompanyDto;
import com.finbite.bilnexserver.auth.utils.constraints.ValidPerson;
import com.finbite.bilnexserver.common.models.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Company model
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Data
@Entity
@ValidPerson
@EqualsAndHashCode(callSuper = true)
public final class Company extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(nullable = false, unique = true)
    private String regCode;

    private String vatNr;
    private String address;
    private String city;
    private String zipcode;
    private String country;

    @JsonProperty("hasSubscribed")
    private boolean hasSubscribed;

    @JsonProperty("isDefault")
    private boolean isDefault;

    @JsonProperty("isActive")
    private boolean isActive;

    public CompanyDto getDto() {
        return new CompanyDto(id, name, regCode, vatNr, address, city, zipcode, country, hasSubscribed, isDefault,
                isActive);
    }
}
