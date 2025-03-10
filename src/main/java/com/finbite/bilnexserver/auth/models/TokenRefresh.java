package com.finbite.bilnexserver.auth.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finbite.bilnexserver.common.models.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Refresh token model
 *
 * @author vinodjohn
 * @created 27.02.2025
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class TokenRefresh extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Person person;

    @JsonProperty("isActive")
    private boolean isActive;
}
