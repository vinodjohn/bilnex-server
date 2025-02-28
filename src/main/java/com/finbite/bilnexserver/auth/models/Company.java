package com.finbite.bilnexserver.auth.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String regCode;
    private String vatNr;
    private String address;
    private String city;
    private int zipcode;
    private boolean hasSubscribed;

    @JsonProperty("isActive")
    private boolean isActive;
}
