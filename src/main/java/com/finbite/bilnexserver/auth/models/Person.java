package com.finbite.bilnexserver.auth.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finbite.bilnexserver.auth.utils.constraints.ValidPerson;
import com.finbite.bilnexserver.common.dtos.UserDto;
import com.finbite.bilnexserver.common.models.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

/**
 * Person model
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Data
@Entity
@ValidPerson
@EqualsAndHashCode(callSuper = true)
public final class Person extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonProperty("isVerified")
    private boolean isVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Company> companies;

    @Enumerated(EnumType.STRING)
    private SystemLanguage defaultSystemLanguage;

    @JsonProperty("isActive")
    private boolean isActive;

    public UserDto toDto() {
        return new UserDto(id, email, password, role.name());
    }
}
